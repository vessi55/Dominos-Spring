package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.ProductDao;
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
public class ProductController extends BaseController{

    public static final double MIN_QUANTITY = 0;
    public static final double MAX_QUANTITY = 100;


    @Autowired
    ProductDao productDao;

    @GetMapping(value = "/pizzas")
    public List<Pizza> getAllPizzas(){
        return productDao.getAllPizzas();
    }

    @PostMapping(value = "/pizzas/add")
    public CommonResponseDTO savePizza(@RequestBody Pizza pizza, HttpSession session) throws BaseException {
        // SessionManager.validateLoginAdmin(session);
        validatePizzaInput(pizza);
        validatePizzaByName(pizza);
        productDao.addProduct(pizza);
        return  new CommonResponseDTO("Pizza with name: " + pizza.getName() + " is successfully added" , LocalDateTime.now());
    }

    @PostMapping(value = "/pizzas/filter")
    public Optional<Pizza> getProductByName(@RequestParam("name") String name) throws BaseException {
        Optional<Pizza> product = productDao.getByName(name);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductException("Product with this name " + name + " does not exists!");
        }
    }

    @PutMapping(value = ("/pizzas/{id}/quantity/{quantity}"))
    public CommonResponseDTO changeProductQuantity(@PathVariable("id") long id, @PathVariable("quantity") int quantity, HttpSession session) throws BaseException {
       // SessionManager.validateLoginAdmin(session);
        if (quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY) {
            productDao.changePizzaQuantity(id, quantity);
        }
        else {
            throw new InvalidInputException("Quantity must be in interval : " + MIN_QUANTITY  +" - " + MAX_QUANTITY + " .");
        }
        return new CommonResponseDTO("Pizza with id: " + id + "has quantity : " + quantity + " ." , LocalDateTime.now());
    }

    @DeleteMapping(value = ("/pizzas/{id}/delete"))
    public CommonResponseDTO deleteProduct(@PathVariable("id") long id, HttpSession session) throws InvalidInputException {
      //  SessionManager.validateLoginAdmin(session);
        productDao.deleteProductById(id);
        return new CommonResponseDTO("Pizza with id: " + id + " was removed! : " , LocalDateTime.now());
    }

    @PostMapping(value = "/pizzas/filter/name")
    public List<Pizza> filterByPrice(@RequestParam("name") String name) throws BaseException {
        List<Pizza> products = productDao.getAllPizzaNamesOrderedByPrice(name);
        if(products.isEmpty()) {
            throw new ProductException("No pizzas found by this name!");
        }
        return products;
    }

    private void validatePizzaInput(Pizza pizza)throws InvalidInputException {
        if(pizza.getName() == null || pizza.getName().isEmpty()| pizza.getDescription() == null ||pizza.getDescription().isEmpty()
                || pizza.getSize() == null
                || pizza.getWeight() < 0 || pizza.getPrice() < 0
                || pizza.getImage_url() == null || pizza.getImage_url().isEmpty()){
            throw new InvalidInputException("Invalid input for the pizza. Please try again! All fields must be filled in!");
        }
    }
    private void validatePizzaByName(Pizza pizza) throws InvalidInputException {
        if (productDao.getProductByName(pizza.getName()) != null) {
            throw new InvalidInputException("Product with that name already exists. You can change its quantity only!");
        }
    }
}
