package estg.ipp.articles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document //(collection = "foto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Foto {
    @Id
    String fotourl;
    @NotNull
    String data;

    public Foto(@NotNull String data) {
        this.data = data;
    }
}
