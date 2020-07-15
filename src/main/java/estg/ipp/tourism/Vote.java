package estg.ipp.tourism;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class Vote {

    @NotNull
    String tipo;
    @NotNull
    String spotid;
    String username;
    Date createdtime;

    public Vote(@NotNull String tipo,@NotNull String spotid) {
        this.tipo = tipo;
        this.spotid = spotid;
        long millis=System.currentTimeMillis();
        this.createdtime  = new Date();
    }

    public Vote(@NotNull String tipo,@NotNull String spotid, String username) {
        this.tipo = tipo;
        this.spotid = spotid;
        this.username = username;
        long millis=System.currentTimeMillis();
        this.createdtime  = new Date();
    }
}
