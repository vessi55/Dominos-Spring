package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.NonPizzaDao;
import dominos.demo.model.pojos.enums.NonPizzaCategory;
import dominos.demo.model.pojos.products.NonPizza;
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
public class NonPizzaController extends BaseController {

    @Autowired
    NonPizzaDao nonPizzaDao;

    public static final double MIN_QUANTITY = 0;
    public static final double MAX_QUANTITY = 100;

    @PostMapping(value = "/products/add")
    public CommonResponseDTO saveProduct(@RequestBody NonPizza nonPizza, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        validateNonPizzaInput(nonPizza);
        nonPizzaDao.addNonPizza(nonPizza);
        return  new CommonResponseDTO("Product with name: " + nonPizza.getName() + " is successfully added" , LocalDateTime.now());
    }

    @GetMapping(value = "/products")
    public List<NonPizza> getAllProducts(){
        return nonPizzaDao.getAllNonPizzas();
    }

    @GetMapping(value = "/products/category")
    public List<NonPizza> getAllProductsInCategory(@RequestParam("category") NonPizzaCategory category) throws BaseException {
        List<NonPizza> products = nonPizzaDao.getAllByCategory(category);
        if(products.isEmpty()) {
            throw new ProductException("No products found in category: " + category);
        }
        return products;
    }

    @GetMapping(value = "/products/filter/category")
    public List<NonPizza> filterByPrice(@RequestParam("category") NonPizzaCategory category) throws ProductException {
        List<NonPizza> products = nonPizzaDao.getAllByCategoryOrderByPrice(category);
        if(products.isEmpty()) {
            throw new ProductException("No pizzas found by this name!");
        }
        return products;
    }

    @GetMapping(value = "/products/filter/price")
    public List<NonPizza> priceLessThan(@RequestParam("price") double price) throws BaseException {
        List<NonPizza> products = nonPizzaDao.getNonPizzasPriceFilter(price);
        if(products.isEmpty()) {
            throw new ProductException("No products found with price less than: " + price);
        }
        return products;
    }

    @GetMapping(value = "/product/id")
    public Optional<NonPizza> getProductById(@RequestParam("id") Long id) throws BaseException {
        Optional<NonPizza> product = nonPizzaDao.getById(id);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductException("Product with id:" + id + " does not exist in database!");
        }
    }

    @GetMapping(value = "/product/name")
    public Optional<NonPizza> getPizzaByName(@RequestParam("name") String name) throws BaseException {
        Optional<NonPizza> product = nonPizzaDao.getByName(name);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductException("Product with name: " + name + " does not exist in database!");
        }
    }

    @DeleteMapping(value = ("/product/{id}/delete"))
    public CommonResponseDTO deleteProduct(@PathVariable("id") long id, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        nonPizzaDao.deleteNonPizzaById(id);
        return new CommonResponseDTO("Product with id: " + id + " was removed! : ", LocalDateTime.now());
    }

    @PutMapping(value = ("/product/{id}/quantity/{quantity}"))
    public CommonResponseDTO updateProductQuantity(@PathVariable("id") long id, @PathVariable("quantity") int quantity, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        if (quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY) {
            nonPizzaDao.changeProductQuantity(id, quantity);
        }
        else {
            throw new InvalidInputException("Quantity must be in interval : " + MIN_QUANTITY  +" - " + MAX_QUANTITY + " .");
        }
        return new CommonResponseDTO("Product with id: " + id + " has quantity : " + quantity + " ." , LocalDateTime.now());
    }

    private void validateNonPizzaInput(NonPizza nonPizza)throws InvalidInputException {
        if(nonPizza.getName().isEmpty() || nonPizza.getPrice() < 0 || nonPizza.getMeasure() < 0){
            throw new InvalidInputException("Invalid input for the product. Please try again! All fields must be filled in!");
        }
    }
}
