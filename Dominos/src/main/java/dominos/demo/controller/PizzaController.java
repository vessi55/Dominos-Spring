package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.DTOs.IngredientResponseDto;
import dominos.demo.model.DTOs.PizzaResponseDto;
import dominos.demo.model.daos.IngredientDao;
import dominos.demo.model.daos.PizzaDao;
import dominos.demo.model.daos.ProductDao;
import dominos.demo.model.enums.Size;
import dominos.demo.model.products.Ingredient;
import dominos.demo.model.products.Pizza;
import dominos.demo.util.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class PizzaController extends BaseController{

    @Autowired
    PizzaDao pizzaDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    IngredientDao ingredientDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static final double MIN_QUANTITY = 0;
    public static final double MAX_QUANTITY = 100;


    @PostMapping(value = "/pizzas/add")
    public CommonResponseDTO savePizza(@RequestBody Pizza pizza, HttpSession session) throws BaseException  {
        SessionManager.validateLoginAdmin(session);
        validatePizzaInput(pizza);
        pizzaDao.addPizza(pizza);
        return  new CommonResponseDTO("Pizza with name: " + pizza.getName() + " is successfully added" , LocalDateTime.now());
    }

    @GetMapping(value = "/pizzas")
    public List<Pizza> getAllPizzas(){
        return pizzaDao.getAllPizzas();
    }

    @GetMapping(value = "/pizzas/filter/name")
    public List<Pizza> filterByPrice(@RequestParam("name") String name) throws ProductException {
        List<Pizza> products = pizzaDao.getPizzasByNameOrderedByPrice(name);
        if(products.isEmpty()) {
            throw new ProductException("No pizzas found by this name!");
        }
        return products;
    }

    @GetMapping(value = "/pizzas/filter/price")
    public List<Pizza> priceLessThan(@RequestParam("price") double price) throws ProductException {
        List<Pizza> products = pizzaDao.getPizzasPriceFilter(price);
        if(products.isEmpty()) {
            throw new ProductException("No pizzas found with price less than: " + price);
        }
        return products;
    }

    @GetMapping(value = "/pizzas/id")
    public Optional<Pizza> getPizzaById(@RequestParam("id") Long id) throws BaseException {
        Optional<Pizza> product = pizzaDao.getById(id);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductException("Pizza with id:" + id + " does not exist in database!");
        }
    }

    @GetMapping(value = "/pizzas/name")
    public Optional<Pizza> getPizzaByName(@RequestParam("name") String name, @RequestParam("size") Size size) throws BaseException {
        Optional<Pizza> product = pizzaDao.getByNameAndSize(name, size);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductException("Pizza with name: " + name + "and size: " + size + " does not exist in database!");
        }
    }

    @DeleteMapping(value = ("/pizzas/{id}/delete"))
    public CommonResponseDTO deletePizza(@PathVariable("id") long id, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        pizzaDao.deletePizzaById(id);
        return new CommonResponseDTO("Pizza with id: " + id + " was removed! : ", LocalDateTime.now());
    }

    @PutMapping(value = ("/pizzas/{id}/quantity/{quantity}"))
    public CommonResponseDTO updatePizzaQuantity(@PathVariable("id") long id, @PathVariable("quantity") int quantity, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        if (quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY) {
            pizzaDao.changePizzaQuantity(id, quantity);
        }
        else {
            throw new InvalidInputException("Quantity must be in interval : " + MIN_QUANTITY  +" - " + MAX_QUANTITY + " .");
        }
        return new CommonResponseDTO("Pizza with id: " + id + " has quantity : " + quantity + " ." , LocalDateTime.now());
    }

   @PostMapping(value = "/pizza/ingredients/{ingredient_id}")
   public IngredientResponseDto setIngredientsToPizza( @PathVariable("ingredient_id") long ingredient_id, HttpSession session) throws BaseException {
       Pizza pizza = (Pizza) session.getAttribute(SessionManager.PIZZA);
       Ingredient ingredient = ingredientDao.getIngredientById(ingredient_id);
       HashMap<Pizza, HashSet<Ingredient>> pizzaExtras =
               (HashMap<Pizza, HashSet<Ingredient>>) session.getAttribute(SessionManager.PIZZA_INGREDIENTS);
       HashSet<Ingredient> ingredients = pizzaExtras.get(pizza);
       if (!ingredients.contains(ingredient)) {
           pizzaExtras.get(pizza).add(ingredient);
       }
       return new IngredientResponseDto(pizza.getName(), pizza.getDescription(),pizza.getSize(),pizza.getWeight(),
               ingredients);

   }
    @DeleteMapping(value = "/pizza/deleteIngredients/{ingredient_id}")
    public IngredientResponseDto deleteIngredientsFromPizza( @PathVariable("ingredient_id") long ingredient_id, HttpSession session) throws BaseException {
        Pizza pizza = (Pizza) session.getAttribute(SessionManager.PIZZA);
        Ingredient ingredient = ingredientDao.getIngredientById(ingredient_id);
        HashMap<Pizza, HashSet<Ingredient>> pizzaExtras =
                (HashMap<Pizza, HashSet<Ingredient>>) session.getAttribute(SessionManager.PIZZA_INGREDIENTS);
        HashSet<Ingredient> ingredients = pizzaExtras.get(pizza);
        if (ingredients.contains(ingredient)) {
            pizzaExtras.get(pizza).remove(ingredient);
        }
        return new IngredientResponseDto(pizza.getName(),
                pizza.getDescription(),pizza.getSize(),pizza.getWeight(),
                ingredients);

    }

    //VIEW PIZZA
    @GetMapping(value = "/pizzas/{id}")
    public PizzaResponseDto viewPizza(@PathVariable ("id") long id, HttpSession session) throws BaseException{
        Pizza pizza = pizzaDao.getProductById(id);
        if(pizza == null) {
            throw new ProductException("Pizza with id:" +pizza.getId() + " does not exist in database!");
        }
        session.setAttribute(SessionManager.PIZZA_INGREDIENTS, new HashMap<Pizza, HashSet<Ingredient>>());
        session.setAttribute(SessionManager.PIZZA, pizza);
        HashMap<Pizza, HashSet<Ingredient>> pizzaExtras =
                (HashMap<Pizza, HashSet<Ingredient>>)session.getAttribute(SessionManager.PIZZA_INGREDIENTS);
        if(!pizzaExtras.containsKey(pizza)){
            pizzaExtras.put(pizza, new HashSet<>());
        }
        return new PizzaResponseDto(pizza.getName(), pizza.getDescription(),
                pizza.getSize(), pizza.getWeight(),pizza.getPrice());
    }
    @PostMapping(value = "/pizzas/favourites")
    public CommonResponseDTO addToFavourites(HttpSession session) throws InvalidLogInException{
        if(SessionManager.isLoggedIn(session)) {
            Pizza pizza = (Pizza) session.getAttribute(SessionManager.PIZZA);
            HashMap<Pizza, HashSet<Ingredient>> pizzaExtras =
                    (HashMap<Pizza, HashSet<Ingredient>>) session.getAttribute(SessionManager.PIZZA_INGREDIENTS);
            long ingredientId;
            if (pizzaExtras.containsKey(pizza)) {
                if (pizzaExtras.get(pizza).size() == 0) {
                    return new CommonResponseDTO("You can add to favourites only pizza with ingredients!",
                            LocalDateTime.now());
                }
                for (Ingredient ingredient : pizzaExtras.get(pizza)) {
                    ingredientId = ingredient.getId();
                    jdbcTemplate.update("INSERT INTO pizza_ingredients(pizza_id, ingredient_id) VALUES(?,?)"
                            , pizza.getId(), ingredientId);
                }
                return new CommonResponseDTO("You successfulyy added pizza " + pizza.getName() + " to favourites!"
                        , LocalDateTime.now());
            }
            return new CommonResponseDTO("Select pizza first and then added it to your favourites! ",
                    LocalDateTime.now());
        }
        throw new InvalidLogInException("You are not logged in!");
    }
    @GetMapping(value = "/pizzas/stats")
    public List<IngredientResponseDto> showPizzaWithAddedIngredients(HttpSession session) throws InvalidLogInException{
        if(SessionManager.isLoggedIn(session)) {
            return productDao.showMyFavOrders();
        }
        throw new InvalidLogInException("Please log in to view all your orders!");
    }
    private void validatePizzaInput(Pizza pizza)throws InvalidInputException {
        if(pizza.getName() == null || pizza.getName().isEmpty()
                || pizza.getDescription() == null || pizza.getDescription().isEmpty()
                || pizza.getSize() == null || pizza.getWeight() < 0 || pizza.getPrice() < 0){
            throw new InvalidInputException("Invalid input for the pizza. Please try again! All fields must be filled in!");
        }
    }
}
