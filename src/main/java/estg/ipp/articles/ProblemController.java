package estg.ipp.articles;

import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import estg.ipp.articles.ProblemRepository;
import estg.ipp.articles.Aviso;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProblemController {


    @Autowired // Váriavel instaciada pela spring framework (BEAN).
    private ProblemModelAssembler assembler;
    @Autowired
    private ProblemRepository problemRepo;
    @Autowired
    private FotoRepository fotoRepo;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void setAssembler(ProblemModelAssembler assembler) {
        this.assembler = assembler;
    }
    public ProblemController(ProblemRepository problemRepo, FotoRepository fotoRepo) {
        this.problemRepo = problemRepo;
        this.fotoRepo = fotoRepo;
    }
    public ProblemController(){ }
    public SimpMessagingTemplate getSimpMessagingTemplate() {
        return simpMessagingTemplate;
    }

    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/aviso") //Recebendo stomp em /app/avisos
    @SendTo("/topic/avisos")
    public Aviso enviaraviso(Aviso avisoObj) throws Exception {
        Thread.sleep(1000); // simulated delay

        Aviso aviso = new Aviso(avisoObj.getTitle(), avisoObj.getDescription());

        /*this.avisoRepo.save(problem)
                .map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build());*/

        return aviso;
    }

    @GetMapping("/problem/{id}")
    public Mono<ResponseEntity<Problem>> getProblem(@PathVariable String id) { //Long
    //public Mono<EntityModel<Problem>> getProblem(@PathVariable String id) {

        return this.problemRepo.findById(id)
                .map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/problems", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Problem> getAllProblems() {   return this.problemRepo.findAll();    }

    @PostMapping("/problem")
    public Mono<ResponseEntity<Problem>> postProblem(@RequestBody Problem newObj) {
        Foto foto = new Foto(newObj.getFotourl());
        Mono<Foto> fo = (Mono<Foto>) this.fotoRepo.save(foto).subscribe();
        String resp = String.valueOf(fo);
        System.out.println("ACHOU A FOTO ID: "+resp);
        try {
            //String resp2 = fo.flatMap(a -> a.fotourl).toString();
            Field a = fo.getClass().getField("fotourl");
            System.out.println(a.toString());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Problem problem = new Problem(newObj.getFotourl(), newObj.getLatitude(), newObj.getLongitude(), newObj.getCategory(), newObj.getTitle(), newObj.getDescription() );

        return this.problemRepo.save(problem)
                .map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PutMapping("/problem/{id}")
    public Mono<ResponseEntity<Problem>> editProblem(@RequestBody Problem problem, @PathVariable String id) {

        return this.problemRepo.findById(id)
                .flatMap(a -> {
                    a.setTitle(problem.title);
                    a.setDescription(problem.description);
                    return this.problemRepo.save(a);
                })
                .map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build());

    }

    @DeleteMapping("/problem/{id}")
    public void deleteProblem(@PathVariable String id) {

        this.problemRepo.findById(id)
                .flatMap(a -> this.problemRepo.deleteById(id).then(Mono.just(ResponseEntity.ok().<Void>build())))
                .defaultIfEmpty(ResponseEntity.badRequest().build());

    }

    @PostMapping("/upvote/{id}")
    public Mono<ResponseEntity<Problem>> upvote(@PathVariable String id, @RequestBody String user) { //Long
        //Verificar se aquele user já upvote

        return this.problemRepo.findById(id)
        .flatMap(a -> {
            a.setUpvote(a.getUpvote()+1);
            return this.problemRepo.save(a);
        })
        .map(a -> ResponseEntity.ok(a))
        .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/downvote/{id}")
    public Mono<ResponseEntity<Problem>> downvote(@PathVariable String id, @RequestBody String user) { //Long
        //Verificar se aquele user já downvote

        return this.problemRepo.findById(id)
        .flatMap(a -> {
            a.setDownvote(a.getDownvote()+1);
            return this.problemRepo.save(a);
        }).map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/solved/{id}")
    public Mono<ResponseEntity<Problem>> upvote(@PathVariable String id) { //Long
        //Verificar se aquele user já upvote

        return this.problemRepo.findById(id)
                .flatMap(a -> {
                    a.setSolved(true);
                    return this.problemRepo.save(a);
                })
                .map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping(value="/upload",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE  )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> upload(@RequestPart("file") FilePart file)
    throws IOException {

        Path tempfile = Files.createFile(Paths.get("src\\main\\resources\\filesBD\\"+file.filename()));

        AsynchronousFileChannel channel = AsynchronousFileChannel.open(tempfile);
        DataBufferUtils.write(file.content(), channel, 0)
                /*.doOnComplete(() -> {
                System.out.println("finish");
                })*/
                .subscribe();
        file.transferTo(tempfile.toFile());
        //String saida = title+" "+description+" "+category+" ("+latitude+","+longitude+")";
        //System.out.println(saida);

        return Mono.just(tempfile.getFileName().toString());

    }

    @GetMapping("files/{fileName}")
    public Mono<Resource> getFile(@PathVariable String fileName) {
        Resource res = new ClassPathResource("\\filesBD\\"+fileName);
        return Mono.just(res);
    }


}