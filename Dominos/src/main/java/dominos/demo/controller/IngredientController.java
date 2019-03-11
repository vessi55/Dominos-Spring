package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.IngredientDao;
import dominos.demo.model.pojos.enums.IngredientCategory;
import dominos.demo.model.pojos.products.Ingredient;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidInputException;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class IngredientController extends BaseController {
    @Autowired
    IngredientDao ingredientDao;

    public static final double MIN_QUANTITY = 0;
    public static final double MAX_QUANTITY = 100;


    @PostMapping(value = "/ingredients/add")
    public CommonResponseDTO saveProduct(@RequestBody Ingredient ingredient, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        validateIngredientInput(ingredient);
        ingredientDao.addIngredient(ingredient);
        return  new CommonResponseDTO("Ingredient with name: " +
                ingredient.getName() + " is successfully added" , LocalDateTime.now());
    }

    @GetMapping(value = "/ingredients")
    public List<Ingredient> getAllIngredients(){
        return ingredientDao.getAllIngredients();
    }

    @GetMapping(value = "/ingredients/id")
    public Optional<Ingredient> getIngredientById(@RequestParam("id") Long id) throws BaseException {
        Optional<Ingredient> product = ingredientDao.getById(id);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductException("Ingredient with id:" + id + " does not exist in database!");
        }
    }

    @DeleteMapping(value = ("/ingredients/{id}/delete"))
    public CommonResponseDTO deleteProduct(@PathVariable("id") long id, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        ingredientDao.deleteIngredientById(id);
        return new CommonResponseDTO("Ingredient with id: " + id + " was removed! : ", LocalDateTime.now());
    }

    @PutMapping(value = ("/ingredients/{id}/quantity/{quantity}"))
    public CommonResponseDTO updateIngredientQuantity(@PathVariable("id") long id, @PathVariable("quantity") int quantity, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        if (quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY) {
            ingredientDao.changeIngredientQuantity(id, quantity);
        }
        else {
            throw new InvalidInputException("Quantity must be in interval : " + MIN_QUANTITY  +" - " + MAX_QUANTITY + " .");
        }
        return new CommonResponseDTO("Ingredient with id: " + id + " has quantity : " + quantity + " ." , LocalDateTime.now());
    }

    @GetMapping(value = "/ingredients/filter/category")
    public List<Ingredient> filterByPrice(@RequestParam("category") IngredientCategory category) throws ProductException {
        List<Ingredient> ingredients = ingredientDao.getAllByIngredientCategory(category);
        if(ingredients.isEmpty()) {
            throw new ProductException("No ingredient found by this name!");
        }
        return ingredients;
    }

    private void validateIngredientInput(Ingredient ingredient)throws InvalidInputException {
        if(ingredient.getName().isEmpty() || ingredient.getName() == null
                || ingredient.getIngredientCategory() == null || ingredient.getPrice() < 0 ){
            throw new
                    InvalidInputException(
                    "Invalid input for the ingredient.Please try again! All fields must be filled in!");
        }
    }
}
