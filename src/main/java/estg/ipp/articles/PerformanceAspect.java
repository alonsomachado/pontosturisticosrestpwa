package estg.ipp.articles;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
@Aspect
public class PerformanceAspect {

    private final LoggingRepository repo;

    @Autowired // Váriavel instaciada pela spring framework (BEAN).
    private StatisticRepository statisticRepo;

    public void setAssembler(StatisticRepository statisticRepo) {
        this.statisticRepo = statisticRepo;
    }

    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    public PerformanceAspect(LoggingRepository repo) {
        this.repo = repo;
    }

    @Pointcut("target(estg.ipp.articles.SpotController)")
    public void problemClassMethods(){}

    @Pointcut("target(estg.ipp.articles.UsuarioController)")
    public void usarioClassMethods(){}

    @Around("problemClassMethods()")
    public Object measureMethodExecutionTimeProblems(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.nanoTime();
        Object retval = pjp.proceed();
        long end = System.nanoTime();

        String methodName = pjp.getSignature().getName();
        long executionTime = TimeUnit.NANOSECONDS.toMillis(end-start);

        Logging logging = new Logging(methodName, executionTime);
        this.repo.save(logging);

        if(this.statisticRepo.existsById(methodName)){

            Map<String, List<Logging>> map = this.repo.findAll().stream()
                    .collect(Collectors.groupingBy(Logging::getMethodName));

            long sum = map.get(methodName).stream().mapToLong(t -> t.executionTime).sum();

            Statistic statistic = this.statisticRepo.findById(methodName).get();
            statistic.numberOfTimeIsInvoked++;
            statistic.executionTimeAvg = sum/map.get(methodName).size();
            this.statisticRepo.save(statistic);

        } else {

            Statistic statistic = new Statistic(methodName, 1, executionTime);
            this.statisticRepo.save(statistic);

        }

        logger.info("Execution of " + methodName + " took " + executionTime + " ms!");

        return retval;

    }

    @Around("usarioClassMethods()")
    public Object measureMethodExecutionTimeUsers(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.nanoTime();
        Object retval = pjp.proceed();
        long end = System.nanoTime();

        String methodName = pjp.getSignature().getName();
        long executionTime = TimeUnit.NANOSECONDS.toMillis(end-start);

        Logging logging = new Logging(methodName, executionTime);
        this.repo.save(logging);

        if(this.statisticRepo.existsById(methodName)){

            Map<String, List<Logging>> map = this.repo.findAll().stream()
                    .collect(Collectors.groupingBy(Logging::getMethodName));

            long sum = map.get(methodName).stream().mapToLong(t -> t.executionTime).sum();

            Statistic statistic = this.statisticRepo.findById(methodName).get();
            statistic.numberOfTimeIsInvoked++;
            statistic.executionTimeAvg = sum/map.get(methodName).size();
            this.statisticRepo.save(statistic);

        } else {

            Statistic statistic = new Statistic(methodName, 1, executionTime);
            this.statisticRepo.save(statistic);

        }

        logger.info("Execution of " + methodName + " took " + executionTime + " ms!");

        return retval;

    }

}
