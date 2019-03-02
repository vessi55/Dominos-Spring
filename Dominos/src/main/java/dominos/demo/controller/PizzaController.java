package dominos.demo.controller;

import dominos.demo.model.DTOs.ResponseDTO;
import dominos.demo.model.daos.PizzaDao;
import dominos.demo.model.products.Pizza;
import dominos.demo.util.exceptions.InvalidInputException;
import dominos.demo.util.exceptions.ProductException;
import dominos.demo.util.exceptions.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class PizzaController  extends BaseController{

    public static final double MIN_QUANTITY = 0;
    public static final double MAX_QUANTITY = 100;


    @Autowired PizzaDao pizzaDao;

    @GetMapping(value = "/pizzas")
    public List<Pizza> getAllPizzas(){
        return pizzaDao.getAllPizas();
    }

    //AS A PARAMETER ->> NAME
    @PostMapping(value = "/products/filter")
    public Optional<Pizza> getProductByName(@RequestParam("name") String name) throws BaseException {
        Optional<Pizza> product = pizzaDao.findPizzaByName(name);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductException("Product with this name " + name + " does not exists!");
        }
    }
    @PostMapping(value = "/products/add")
    public ResponseDTO savePizza(@RequestBody Pizza pizza, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        validateProductInput(pizza);
        validatePizzaByName(pizza);
        pizzaDao.addPizza(pizza);
        return  new ResponseDTO("Pizza" + pizza.getName() + "is successfully added" , LocalDateTime.now());
    }

    private void validateProductInput(Pizza pizza)throws BaseException {
        if(pizza.getName() == null || pizza.getDescription() == null || pizza.getSize() == null
                || pizza.getWeight() < 0 || pizza.getPrice() < 0
                || pizza.getImage_url() == null){
            throw new InvalidInputException("Invalid input for the pizza. Please try again");
        }
    }
    private void validatePizzaByName(Pizza pizza) throws BaseException {
        if (getPizzaByName(pizza.getName()) != null) {
            throw new InvalidInputException("Product with that name already exists. You can change its quantity only!");
        }
    }

    private Pizza getPizzaByName(String name) {
        Optional<Pizza> product = pizzaDao.findPizzaByName(name);
        if (product.isPresent()) {
            return product.get();
        }
        else {
            return null;
        }
    }
    @PutMapping(value = ("/product/{id}/quantity/{quantity}"))
    public ResponseDTO changeProductQuantity(@PathVariable("id") long id, @PathVariable("quantity") int quantity, HttpSession session) throws Exception {
        SessionManager.validateLoginAdmin(session);
        if (quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY) {
            pizzaDao.changePizzaQuantity(id, quantity);
        }
        else {
            throw new InvalidInputException("Quantity must be in interval : " + MIN_QUANTITY  +" - " + MAX_QUANTITY + " .");
        }
        return new ResponseDTO("Pizza with id: " + id + "has quantity : " + quantity + " ." , LocalDateTime.now());
    }

    @PutMapping(value = ("/product/{id}/delete"))
    public ResponseDTO deleteProduct(@PathVariable("id") long id, HttpSession session) throws Exception {
        SessionManager.validateLoginAdmin(session);
        pizzaDao.deletePizza(id);
        return new ResponseDTO("Pizza with id: " + id + " was removed! : " , LocalDateTime.now());
    }


}
