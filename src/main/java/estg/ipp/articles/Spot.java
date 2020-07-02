package estg.ipp.articles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "touristattraction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spot {

    //@MongoId
    @Id
    //@Field("_id")
    String id;
    String fotourl; //Guardar URL da Foto
    String latitude;
    String longitude;
    String website;
    String cidade;
    @NotNull
    String title;
    @NotNull
    String description;
    Integer upvote;
    Integer downvote;
    Boolean disablecomment;
    ArrayList<String> comentarios;
    Date createdtime;

    public Spot(String fotourl, String latitude, String longitude, String website, String cidade, String title, String description) {

        this.fotourl = fotourl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.upvote = 0;
        this.downvote = 0;
        this.disablecomment = false;
        this.website = website;
        this.cidade = cidade;
        this.title = title;
        this.comentarios = new ArrayList<String>();
        this.description = description;
        this.createdtime = new Date();
    }
    public void addComment(String comentario){
        this.comentarios.add(comentario);
    }
}
