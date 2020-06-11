package estg.ipp.articles;

import org.springframework.data.jpa.repository.JpaRepository;

//import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface StatisticRepository extends JpaRepository<Statistic, String> {
//public interface StatisticRepository extends ReactiveCrudRepository<Statistic, String> {
}