package dominos.demo.model.repositories;

import dominos.demo.model.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    List<Product> findAll();

    @Override
    Optional<Product> findById(Long aLong);

    Optional<Product> findByName(String name);

    List<Product> findAllByCategoryOrderByPrice(String category);
}
