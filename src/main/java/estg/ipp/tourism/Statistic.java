package estg.ipp.tourism;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.annotation.Id;

//@Document
@Data
@Entity
public class Statistic {

    @Id
    String methodName;
    long numberOfTimeIsInvoked;
    double executionTimeAvg;

    public Statistic(){
    }

    public Statistic(String methodName, long numberOfTimeIsInvoked, double executionTimeAvg){
        this.methodName = methodName;
        this.numberOfTimeIsInvoked = numberOfTimeIsInvoked;
        this.executionTimeAvg = executionTimeAvg;
    }

    public String getMethodName(){
        return this.methodName;
    }
    public void setMethodName(String methodName){
        this.methodName = methodName;
    }
    public long getNumberOfTimeIsInvoked(){
        return this.numberOfTimeIsInvoked;
    }
    public void setNumberOfTimeIsInvoked(long numberOfTimeIsInvoked){        this.numberOfTimeIsInvoked = numberOfTimeIsInvoked;    }
    public double getExecutionTimeAvg(){
        return this.executionTimeAvg;
    }
    public void setExecutionTimeAvg(double executionTimeAvg){
        this.executionTimeAvg = executionTimeAvg;
    }

}
