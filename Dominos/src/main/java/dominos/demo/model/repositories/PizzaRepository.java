package dominos.demo.model.repositories;

import dominos.demo.model.enums.Size;
import dominos.demo.model.products.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza,Long>{

    @Override
    List<Pizza> findAll();

    List<Pizza> findAllByNameOrderByPrice(String name);

    List<Pizza> findAllByName(String name);

    List<Pizza> findAllByPriceLessThan(double price);

    @Override
    Optional<Pizza> findById(Long aLong);

    Optional<Pizza> findByNameAndSize(String name, Size size);

    @Override
    void delete(Pizza pizza);


}
