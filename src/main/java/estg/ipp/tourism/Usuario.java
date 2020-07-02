package estg.ipp.tourism;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
@NoArgsConstructor
@Document //(collection = "usuario") para ficar minusculo
public class Usuario {

    @Id @NotNull String username;
    @NotNull String password;
    Date criado;
    Boolean isAdmin;

    public Usuario(@NotNull String username, @NotNull String password) {
        this.username = username;
        this.password = password;
        this.criado  = new Date();
        this.isAdmin = false;
    }

    public Usuario getUsuario() {
        return this;
    }

    public boolean getIsAdmin() {
        return this.isAdmin;
    }
}
