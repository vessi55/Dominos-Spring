package dominos.demo.model.repositories;


import dominos.demo.model.products.Non_Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonPizzaRepository extends JpaRepository<Non_Pizza, Long> {
}
