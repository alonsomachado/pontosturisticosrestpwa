package estg.ipp.articles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@NoArgsConstructor
public class Aviso {

    @NotNull
    String title;
    @NotNull
    String description;
    Date time;

    public Aviso(@NotNull String title, @NotNull String description) {
        this.title = title;
        this.description = description;
        long millis=System.currentTimeMillis();
        time  = new java.sql.Date(millis);
    }
}
