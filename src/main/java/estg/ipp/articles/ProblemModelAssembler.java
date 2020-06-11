package estg.ipp.articles;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//HATEOAS para response com as operações getID e TODOS
@Component
public class ProblemModelAssembler implements RepresentationModelAssembler<Problem, EntityModel<Problem>> {

    @Override
    public EntityModel<Problem> toModel(Problem problem) {
        return new EntityModel<>(problem,
                linkTo(methodOn(ProblemController.class).getProblem(problem.getId())).withSelfRel(),
                linkTo(methodOn(ProblemController.class).getAllProblems()).withRel("problems"));
    }
}
