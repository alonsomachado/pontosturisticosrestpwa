package estg.ipp.tourism;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class Comentario {

    @NotNull
    String comentario;
    @NotNull
    String spotid;
    String userid;
    Date createdtime;

    public Comentario(@NotNull String comentario,@NotNull String spotid) {
        this.comentario = comentario;
        this.spotid = spotid;
        long millis=System.currentTimeMillis();
        this.createdtime  = new Date();
    }
}
