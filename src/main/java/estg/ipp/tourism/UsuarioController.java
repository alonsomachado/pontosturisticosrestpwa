package estg.ipp.tourism;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
public class UsuarioController {

    private final UsuarioRepository userrepo;

    public UsuarioController(UsuarioRepository userrepo) {
        this.userrepo = userrepo;
    }

    @CrossOrigin
    @PostMapping("/login")
    public Mono<ResponseEntity<?>> loginUsuario(@RequestBody Usuario newUser) {

        Usuario novo = new Usuario(newUser.getUsername(), newUser.getPassword());

        //Verificar se já existe e verifica senha
        return this.userrepo.findById(novo.getUsername())
        .map((salvo) -> {
            if (salvo.getPassword().equals(novo.getPassword())) {
                return ResponseEntity.ok(salvo); //Mono.just(novo)
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //Erro 401 Não bateu senha
            }
        }).defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @CrossOrigin
    @PostMapping("/register")
    public Mono<? extends ResponseEntity<? extends Object>> register(@RequestBody Usuario newUser) {

        Usuario novo = new Usuario(newUser.getUsername(), newUser.getPassword());

        if(this.userrepo.existsById(novo.getUsername()).block() ) {
            return this.userrepo.findById(novo.getUsername())
                    .map((salvo) -> {
                        if (salvo.getUsername().equals(novo.getUsername())) {
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //Ja existe esse Usuario
                        } else {
                            return ResponseEntity.status(402).build(); //Não entra NUNCA
                        }
                    }).defaultIfEmpty(ResponseEntity.status(418).build());
        }else{
            //Não tem Usuario com esse login então CRIAR novo
            return this.userrepo.save(novo)
                    //.map(a -> ResponseEntity.status(201).build()) //201 Criado
                    .map(a -> ResponseEntity.ok(novo)) //Mandar o Usuario criado
                    .defaultIfEmpty(ResponseEntity.badRequest().build());
            //.subscribe() //Salva Usuario mas altera todos inclusive os das condicoes
        }

    }

    @PostMapping("/loginadmin")
    public Mono<ResponseEntity<?>> loginAdmin(@RequestBody Usuario newUser) {

        Usuario novo = new Usuario(newUser.getUsername(), newUser.getPassword());

        //Verificar se já existe
        return this.userrepo.findById(novo.getUsername())
                .map((salvo) -> {
                    if (salvo.getPassword().equals(novo.getPassword()) && salvo.getIsAdmin()) {
                        return ResponseEntity.ok(salvo); //Mono.just(novo)
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //Erro 401 Não bateu senha
                    }
                }).defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
