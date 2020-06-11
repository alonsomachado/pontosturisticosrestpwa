package estg.ipp.articles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document //(collection = "foto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Foto {
    @Id
    String fotourl;
    @NotNull
    String data;
    Date createdtime;

    public Foto(@NotNull String data) {
        this.data = data;
        this.createdtime = new Date();
    }
}
