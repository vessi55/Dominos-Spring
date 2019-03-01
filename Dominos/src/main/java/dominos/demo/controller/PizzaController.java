package dominos.demo.controller;

import dominos.demo.model.daos.PizzaDao;
import dominos.demo.model.products.Pizza;
import dominos.demo.util.exceptions.InvalidInputException;
import dominos.demo.util.exceptions.ProductNotFoundException;
import dominos.demo.util.exceptions.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
public class PizzaController  extends BaseController{

    @Autowired PizzaDao pizzaDao;

    @GetMapping(value = "/pizzas")
    public List<Pizza> getAllPizzas(){
        return pizzaDao.getAllPizas();
    }

    @PostMapping(value = "/products/filter")
    public Optional<Pizza> getProductByName(@RequestParam("name") String name) throws BaseException {
        Optional<Pizza> product = pizzaDao.findPizzaByName(name);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductNotFoundException();
        }
    }
    //TODO: return CommonMessage
    @PostMapping(value = "/products/add")
    public Pizza save(@RequestBody Pizza pizza, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        validateProductInput(pizza);
        pizzaDao.addPizza(pizza);
        return pizza;
    }

    private void validateProductInput(Pizza pizza)throws BaseException {
        if(pizza.getName() == null || pizza.getDescription() == null || pizza.getSize() == null
                || pizza.getWeight() < 0 || pizza.getPrice() < 0
                || pizza.getImage_url() == null){
            throw new InvalidInputException("Invalid input for the pizza. Please try again");
        }
        //TODO : more validations
    }

}
