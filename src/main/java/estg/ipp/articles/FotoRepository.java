package estg.ipp.articles;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepository extends ReactiveCrudRepository<Foto, String> {

}

