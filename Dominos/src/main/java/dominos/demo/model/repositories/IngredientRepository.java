package dominos.demo.model.repositories;

import dominos.demo.model.products.Ingredients;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends ProductRepository<Ingredients> {
}
