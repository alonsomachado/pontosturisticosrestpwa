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
    Date createdtime;

    public Vote(@NotNull String tipo,@NotNull String spotid) {
        this.tipo = tipo;
        this.spotid = spotid;
        long millis=System.currentTimeMillis();
        this.createdtime  = new Date();
    }
}
