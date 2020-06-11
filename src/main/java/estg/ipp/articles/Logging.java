package estg.ipp.articles;

import lombok.Data;
//import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
//@Document
@Data
@Entity
public class Logging {

    @Id
    @GeneratedValue
    long id;
    String methodName;
    long executionTime;

    public Logging(){

    }

    public Logging(String methodName, long executionTime){
        this.methodName = methodName;
        this.executionTime = executionTime;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getMethodName(){
        return methodName;
    }

    public void setMethodName(String methodName){
        this.methodName = methodName;
    }

    public long getExecutionTime(){
        return executionTime;
    }

    public void setExecutionTime(long executionTime){
        this.executionTime = executionTime;
    }

}
