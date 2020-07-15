package estg.ipp.tourism;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

@Data
@NoArgsConstructor
public class VotedSpots {

    @NotNull
    Hashtable<String,ArrayList<String>> hashvotes;
    //ArrayList<String> users;
    public Boolean addVotedSpot(@NotNull String username, @NotNull String spotid) {

        if(this.hashvotes.contains(spotid)){
            ArrayList<String> temp = this.hashvotes.get(spotid);
            if(!temp.contains(username)){ //Se não tem este username
                this.hashvotes.get(spotid).add(username); //Adiciona na lista
                return true;
            }
        }else{ //Não tem VOTE daquele PONTO TURISTICO
            ArrayList<String> novo = new ArrayList<String>();
            novo.add(username);
            this.hashvotes.put(spotid,novo); //Adiciona na lista
            return true;
        }
        return false; //Já existia voto deste username
    }

}
