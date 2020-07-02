package estg.ipp.tourism;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LoggingRepository extends JpaRepository<Logging, String> {
//public interface LoggingRepository extends ReactiveCrudRepository<Logging, String> {
}
