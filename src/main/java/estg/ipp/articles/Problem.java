package estg.ipp.articles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "problem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    //@MongoId
    @Id
    //@Field("_id")
    String id;
    String fotourl; //Guardar URL da Foto
    String latitude;
    String longitude;
    String category;
    @NotNull
    String title;
    @NotNull
    String description;
    Integer upvote;
    Integer downvote;
    Boolean solved;
    Date createdtime;

    public Problem(String fotourl, String latitude, String longitude, String category, String title, String description) {

        this.fotourl = fotourl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.upvote = 0;
        this.downvote = 0;
        this.solved = false;
        this.category = category;
        this.title = title;
        this.description = description;
        this.createdtime = new Date();
    }
}
