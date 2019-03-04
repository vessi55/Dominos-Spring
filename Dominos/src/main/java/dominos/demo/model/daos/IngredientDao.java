package dominos.demo.model.daos;

//import dominos.demo.model.products.Ingredient;
//import dominos.demo.model.products.Pizza;
//import dominos.demo.model.repositories.IngredientRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Optional;
//
//@Component
//public class IngredientDao {
//    @Autowired
//    IngredientRepository ingredientRepository;
//
//    public void addIngredient(Ingredient ingredient){
//        ingredientRepository.save(ingredient);
//    }
//    public void getAllIngredients(){
//        ingredientRepository.findAll();
//    }
//    public Ingredient getById(long id){
//        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
//        if (ingredient.isPresent()) {
//            return ingredient.get();
//        }
//        else {
//            return null;
//        }
//    }
//    public List<Ingredient> getAllByIdOrderByCategory(long id){
//        return ingredientRepository.findAllByIdOrderByIngredientCategory(id);
//    }
//
//}
