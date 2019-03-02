package dominos.demo.model.repositories;

import dominos.demo.model.products.Non_Pizza;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.products.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository<T extends Product> extends JpaRepository<T,Long> {
    @Override
    List<T> findAll();
}
