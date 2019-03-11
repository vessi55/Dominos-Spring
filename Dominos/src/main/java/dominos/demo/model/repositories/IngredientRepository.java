package dominos.demo.model.repositories;


import dominos.demo.model.pojos.enums.IngredientCategory;
import dominos.demo.model.pojos.products.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Override
    List<Ingredient> findAll();

    @Override
    Optional<Ingredient> findById(Long aLong);

    List<Ingredient> findAllByIngredientCategory(IngredientCategory ingredientCategory);

    @Override
    void delete(Ingredient ingredient);
}
