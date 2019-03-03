package dominos.demo.model.repositories;

import dominos.demo.model.products.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PizzaRepository extends JpaRepository<Pizza,Long>{
    @Override
    List<Pizza> findAll();

    @Override
    Optional<Pizza> findById(Long aLong);
    Optional<Pizza> findByName(String name);

    @Override
    void delete(Pizza pizza);
    List<Pizza> findAllByNameOrderByPrice(String name);
}
