package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.IngredientDao;
import dominos.demo.model.daos.PizzaDao;
import dominos.demo.model.enums.Size;
import dominos.demo.model.products.Ingredient;
import dominos.demo.model.products.Pizza;
import dominos.demo.util.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class PizzaController extends BaseController{

    @Autowired
    PizzaDao pizzaDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    IngredientDao ingredientDao;

    public static final double MIN_QUANTITY = 0;
    public static final double MAX_QUANTITY = 100;

    public static final String SHOPPING_CART = "cart";
    public static final String USER = "user";


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
       // SessionManager.validateLoginAdmin(session);
        if (quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY) {
            pizzaDao.changePizzaQuantity(id, quantity);
        }
        else {
            throw new InvalidInputException("Quantity must be in interval : " + MIN_QUANTITY  +" - " + MAX_QUANTITY + " .");
        }
        return new CommonResponseDTO("Pizza with id: " + id + " has quantity : " + quantity + " ." , LocalDateTime.now());
    }
    @PostMapping(value = "/pizzas/{pizza_id}/ingredients/{ingredient_id}")
    public void setIngredientsToPizza(@PathVariable("pizza_id") long pizza_id, @PathVariable("ingredient_id") long ingredient_id, HttpSession session) throws BaseException {
        Pizza pizza = pizzaDao.getProductById(pizza_id);
        Ingredient ingredient = ingredientDao.getIngredientById(ingredient_id);
        session.setAttribute("ingredients", new HashMap<Pizza, HashSet<Ingredient>>());
        HashMap<Pizza, HashSet<Ingredient>> pizzaExtras = (HashMap<Pizza, HashSet<Ingredient>>)session.getAttribute("ingredients");
        if(!pizzaExtras.containsKey(pizza)){
            pizzaExtras.put(pizza, new HashSet<>());
        }
        pizzaExtras.get(pizza).add(ingredient);
    }



    private void validatePizzaInput(Pizza pizza)throws InvalidInputException {
        if(pizza.getName() == null || pizza.getName().isEmpty()
                || pizza.getDescription() == null || pizza.getDescription().isEmpty()
                || pizza.getSize() == null || pizza.getWeight() < 0 || pizza.getPrice() < 0){
            throw new InvalidInputException("Invalid input for the pizza. Please try again! All fields must be filled in!");
        }
    }
}
