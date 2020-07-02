package estg.ipp.articles;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//HATEOAS para response com as operações getID e TODOS
@Component
public class SpotModelAssembler implements RepresentationModelAssembler<Spot, EntityModel<Spot>> {

    @Override
    public EntityModel<Spot> toModel(Spot spot) {
        return new EntityModel<>(spot,
                linkTo(methodOn(SpotController.class).getSpot(spot.getId())).withSelfRel(),
                linkTo(methodOn(SpotController.class).getAllSpots()).withRel("spots"));
    }
}
