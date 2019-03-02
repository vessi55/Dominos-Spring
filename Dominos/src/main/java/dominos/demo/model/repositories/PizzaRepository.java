package dominos.demo.model.repositories;

import dominos.demo.model.products.Pizza;
import dominos.demo.model.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PizzaRepository extends ProductRepository<Pizza>{
    Optional<Pizza> findByName(String name);
}
