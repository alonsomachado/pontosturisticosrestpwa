package estg.ipp.articles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class Aviso {

    @NotNull
    String title;
    @NotNull
    String description;
    Date createdtime;

    public Aviso(@NotNull String title, @NotNull String description) {
        this.title = title;
        this.description = description;
        long millis=System.currentTimeMillis();
        this.createdtime  = new Date();
    }
}
