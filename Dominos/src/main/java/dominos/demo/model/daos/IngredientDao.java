package dominos.demo.model.daos;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.enums.IngredientCategory;
import dominos.demo.model.products.Ingredient;
import dominos.demo.model.repositories.IngredientRepository;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidInputException;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class IngredientDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    IngredientRepository ingredientRepository;

    private static final String UPDATE_QUANTITY = "UPDATE ingredients SET quantity = ? WHERE id = ?";


    public void addIngredient(Ingredient ingredient){
        ingredientRepository.save(ingredient);
    }

    public List<Ingredient> getAllIngredients(){
        return ingredientRepository.findAll();
    }

    public Optional<Ingredient> getById(long id){
        return ingredientRepository.findById(id);
    }

    public Ingredient getIngredientById(long id){
        Optional<Ingredient> product = ingredientRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        }
        else {
            return null;
        }
    }

    public List<Ingredient> getAllByIngredientCategory(IngredientCategory category){
        return ingredientRepository.findAllByIngredientCategory(category);
    }

    public CommonResponseDTO deleteIngredientById(long id) throws InvalidInputException {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if(ingredient.isPresent()) {
            ingredientRepository.delete(ingredient.get());
            return new CommonResponseDTO(ingredient.get().getName()  + " was successfully deleted from database!", LocalDateTime.now());
        }
        throw new InvalidInputException("Product with id:" + id + " does not exist in database.");
    }
    public void changeIngredientQuantity(long id, int quantity) throws BaseException {
        if(checkIfIngredientExists(id)){
            jdbcTemplate.update(UPDATE_QUANTITY, quantity, id);
        }
    }

    public boolean checkIfIngredientExists(long id) throws BaseException {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if(ingredient.isPresent()){
            return true;
        }
        throw new ProductException("The product does not exist! Please add it first!");
    }

}
