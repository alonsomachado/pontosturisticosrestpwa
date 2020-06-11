package estg.ipp.articles;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

//@Component
@Aspect
public class LoggingAspect {

    private final StatisticRepository repo;

    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    public LoggingAspect(StatisticRepository repo) {
        this.repo = repo;
    }

    @Pointcut("target(estg.ipp.articles.ProblemController)") //ProblemRepository
    public void repositoryMethods(){}

    @Before("repositoryMethods()")
    public void logMethodCall(JoinPoint jp){

        String methodName = jp.getSignature().getName();

        if(this.repo.existsById(methodName)){
            this.repo.findById(methodName).get().numberOfTimeIsInvoked++;
        } else {
            Statistic statistic = new Statistic(methodName,0,0d);
            statistic.numberOfTimeIsInvoked++; //Primeira invocacao do metodo numberOfTimeIsInvoked vai para 1
            this.repo.save(statistic);
        }

        logger.info(methodName + ": " +
                this.repo.findById(methodName).get().numberOfTimeIsInvoked +
                " time(s) is Invoked!");

    }

}
