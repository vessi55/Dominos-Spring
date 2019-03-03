package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.ProductDao;
import dominos.demo.model.products.Ingredients;
import dominos.demo.model.products.Non_Pizza;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.products.Product;
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
    public List<Pizza> getAllPizzas() {
        return productDao.getAllPizzas();
    }
    @GetMapping(value = "/non-pizzas")
    public List<Non_Pizza> getAllNonPizzas() {
        return productDao.getAllNonPizzas();
    }
    @GetMapping(value = "/ingredients")
    public List<Ingredients> getAllIngredients() {
        return productDao.getAllIngredients();
    }

    @PostMapping(value = "/products/add")
    public CommonResponseDTO saveProduct(@RequestBody Product product, HttpSession session) throws BaseException {
        // SessionManager.validateLoginAdmin(session);
        // validateProductInput(pizza);
        // validatePizzaByName(pizza);
        productDao.addProduct(product);
        return  new CommonResponseDTO("Product with name: " + product.getName() + "is successfully added" , LocalDateTime.now());
    }

//    @PostMapping(value = "/products/filter")
//    public Optional<Product> getProductByName(@RequestParam("name") String name) throws BaseException {
//        Optional<Product> product = productDao.findProductByName(name);
//        if(product.isPresent()) {
//            return product;
//        }
//        else {
//            throw new ProductException("Product with this name " + name + " does not exists!");
//        }
//    }

    @PutMapping(value = ("/product/{id}/quantity/{quantity}"))
    public CommonResponseDTO changeProductQuantity(@PathVariable("id") long id, @PathVariable("quantity") int quantity, HttpSession session, String table) throws Exception {
        SessionManager.validateLoginAdmin(session);
        if (quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY) {
            productDao.changeProductQuantity(id, quantity, table);
        }
        else {
            throw new InvalidInputException("Quantity must be in interval : " + MIN_QUANTITY  +" - " + MAX_QUANTITY + " .");
        }
        return new CommonResponseDTO("Pizza with id: " + id + "has quantity : " + quantity + " ." , LocalDateTime.now());
    }

    @PutMapping(value = ("/product/{id}/delete"))
    public CommonResponseDTO deleteProduct(@PathVariable("id") long id, HttpSession session, String table) throws Exception {
        SessionManager.validateLoginAdmin(session);
        productDao.deleteProductById(id, table);
        return new CommonResponseDTO("Pizza with id: " + id + " was removed! : " , LocalDateTime.now());
    }

//    private void validateProductInput(Pizza pizza)throws BaseException {
//        if(pizza.getName() == null || pizza.getDescription() == null || pizza.getSize() == null
//                || pizza.getWeight() < 0 || pizza.getPrice() < 0
//                || pizza.getImage_url() == null){
//            throw new InvalidInputException("Invalid input for the pizza. Please try again");
//        }
//    }

//    private void validatePizzaByName(Pizza pizza) throws BaseException {
//        if (productDao.findProductByName(pizza.getName()) != null) {
//            throw new InvalidInputException("Product with that name already exists. You can change its quantity only!");
//        }
  //  }

//    private Product getPizzaByName(String name) {
//        Optional<Product> product = productDao.findProductByName(name);
//        if (product.isPresent()) {
//            return product.get();
//        }
//        else {
//            return null;
//        }
//    }
}
