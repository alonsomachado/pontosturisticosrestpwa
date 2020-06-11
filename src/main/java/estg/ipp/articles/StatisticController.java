package estg.ipp.articles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class StatisticController {

    private final StatisticRepository repo;

    public StatisticController(StatisticRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/statistic/{methodName}")
    public EntityModel<Statistic> getStatistic(@PathVariable String methodName) {

        Statistic s = this.repo.findById(methodName).get();

        EntityModel<Statistic> resp = new EntityModel<>(s,
                linkTo(methodOn(StatisticController.class).getStatistic(methodName)).withSelfRel(),
                linkTo(methodOn(StatisticController.class).getAllStatistics()).withRel("statistic"));

        return resp;

    }

    @GetMapping("/statistics")
    public List<Statistic> getAllStatistics() {
        return this.repo.findAll();
    }

    @GetMapping("/statisticshateoas")
    public CollectionModel<EntityModel<Statistic>> getAllStatistichateoas() {

        List<EntityModel<Statistic>> statistics = this.repo.findAll().stream()
                .map(statistic -> new EntityModel<>(statistic,
                        linkTo(methodOn(StatisticController.class).getStatistic(statistic.getMethodName())).withSelfRel(),
                        linkTo(methodOn(StatisticController.class).getAllStatistics()).withRel("statistics")))
                .collect(Collectors.toList());

        return new CollectionModel<>(statistics,
                linkTo(methodOn(ProblemController.class).getAllProblems()).withSelfRel());

    }
}
