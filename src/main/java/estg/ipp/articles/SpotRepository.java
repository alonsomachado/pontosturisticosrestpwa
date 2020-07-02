package estg.ipp.articles;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotRepository extends ReactiveCrudRepository<Spot, String> {


}

