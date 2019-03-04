package dominos.demo.model.repositories;

import dominos.demo.model.enums.NonPizzaCategory;
import dominos.demo.model.products.NonPizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NonPizzaRepository extends JpaRepository<NonPizza, Long> {

    @Override
    List<NonPizza> findAll();

    List<NonPizza> findAllByCategory(NonPizzaCategory category);

    List<NonPizza> findAllByCategoryOrderByPrice(NonPizzaCategory category);

    List<NonPizza> findAllByPriceLessThan(double price);

    @Override
    Optional<NonPizza> findById(Long aLong);

    Optional<NonPizza> findByName(String name);

    @Override
    void delete(NonPizza non_pizza);

}
