package estg.ipp.tourism;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class SpotController {


    @Autowired // Váriavel instaciada pela spring framework (BEAN).
    private SpotModelAssembler assembler;
    @Autowired
    private SpotRepository spotRepository;
    @Autowired
    private FotoRepository fotoRepo;
    @Autowired
    private UsuarioRepository userRepo;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void setAssembler(SpotModelAssembler assembler) {
        this.assembler = assembler;
    }
    public SpotController(SpotRepository spotRepository, FotoRepository fotoRepo, UsuarioRepository userRepo) {
        this.spotRepository = spotRepository;
        this.fotoRepo = fotoRepo;
        this.userRepo = userRepo;
    }
    public SpotController(){ }
    public SimpMessagingTemplate getSimpMessagingTemplate() {
        return simpMessagingTemplate;
    }

    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    //@CrossOrigin
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

    @MessageMapping("/vote") //Recebendo stomp em /app/vote
    @SendTo("/topic/vote")
    public Vote enviaraviso(Vote voteObj) throws Exception {
        Thread.sleep(1000); // simulated delay

        Vote vote = new Vote(voteObj.getTipo(),voteObj.getSpotid());

        return vote;
    }

    @MessageMapping("/comentario") //Recebendo stomp em /app/comment
    @SendTo("/topic/comentario")
    public Comentario enviarcomentario(Comentario cmtObj) throws Exception {
        Thread.sleep(1000); // simulated delay

        Comentario cmt = new Comentario(cmtObj.getComentario(),cmtObj.getSpotid());
        this.spotRepository.findById(cmt.spotid)
                .flatMap(a -> {
                    a.addComment(cmt.comentario);
                    return this.spotRepository.save(a);
                })
                .map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build()).block(); //Sem o BLOCK ou SUBSCRIBE não executa

        return cmt;
    }

    @CrossOrigin
    @GetMapping("/spot/{id}")
    public Mono<ResponseEntity<Spot>> getSpot(@PathVariable String id) { //Long
    //public Mono<EntityModel<Problem>> getProblem(@PathVariable String id) {

        return this.spotRepository.findById(id)
                .map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @CrossOrigin
    @GetMapping(value = "/spots", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Spot> getAllSpots() {   return this.spotRepository.findAll();    }

    @PostMapping("/spot")
    public Mono<ResponseEntity<Spot>> postSpot(@RequestBody Spot newObj) {
        Foto foto = new Foto(newObj.getFotourl());
        Foto salva = this.fotoRepo.save(foto).block();

        Spot spot = new Spot(newObj.getFotourl(), newObj.getLatitude(), newObj.getLongitude(), newObj.getWebsite(), newObj.getCidade(), newObj.getTitle(), newObj.getDescription() );

        return this.spotRepository.save(spot)
                .map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

     //Desativar o PUT não estou utilizando
    @PutMapping("/spot/{id}")
    public Mono<ResponseEntity<Spot>> editSpot(@RequestBody Spot spot, @PathVariable String id) {

        return this.spotRepository.findById(id)
                .flatMap(a -> {
                    a.setTitle(spot.title);
                    a.setDescription(spot.description);
                    return this.spotRepository.save(a);
                })
                .map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build());

    }

    @DeleteMapping("/spot/{id}")
    public void deleteSpot(@PathVariable String id) {

        this.spotRepository.findById(id)
                .flatMap(a -> this.spotRepository.deleteById(id).then(Mono.just(ResponseEntity.ok().<Void>build())))
                .defaultIfEmpty(ResponseEntity.badRequest().build());

    }

    @CrossOrigin({ "https://alonsomachado.gitlab.io/" })
    @PostMapping("/upvote/{id}")
    public Mono<ResponseEntity<Spot>> upvote(@PathVariable String id, @RequestBody String dados) {
        //Verificar se aquele user já upvote

        System.out.println("Usuário tentando upvote:"+dados);
        if (dados == "user") {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }else {
            return this.spotRepository.findById(id)
                    .flatMap(a -> {
                        a.setUpvote(a.getUpvote() + 1);
                        return this.spotRepository.save(a);
                    })
                    .map(a -> ResponseEntity.ok(a))
                    .defaultIfEmpty(ResponseEntity.badRequest().build());
        }
    }

    @CrossOrigin({ "https://alonsomachado.gitlab.io/" })
    @PostMapping("/downvote/{id}")
    public Mono<ResponseEntity<Spot>> downvote(@PathVariable String id, @RequestBody String user) {
        //Verificar se aquele user já downvote

        System.out.println("Usuário tentando downvote:"+user);

        return this.spotRepository.findById(id)
        .flatMap(a -> {
            a.setDownvote(a.getDownvote()+1);
            return this.spotRepository.save(a);
        }).map(a -> ResponseEntity.ok(a))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/commentblock/{id}")
    public Mono<ResponseEntity<Spot>> disablecomment(@PathVariable String id, @RequestBody String user) {
        //Desabilitar os comentários desse Ponto Turistico e SOMENTE SE USER é ADMIN

        if(this.userRepo.existsById(user).block()){
            this.userRepo.findById(user)
                    .map((salvo) -> {
                        if (salvo.getIsAdmin() == true) {
                            return this.spotRepository.findById(id)
                                    .flatMap(a -> {
                                        a.setDisablecomment(true); //Desabilita o Comentario
                                        return this.spotRepository.save(a);
                                    })
                                    .map(a -> ResponseEntity.ok(a))
                                    .defaultIfEmpty(ResponseEntity.badRequest().build());
                        }
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //Não é admin esse USER
                    });
        }

        return Mono.just(ResponseEntity.status(418).build()); //I am a TEA POT Status não é pra chegar NUNCA AQUI
    }

}