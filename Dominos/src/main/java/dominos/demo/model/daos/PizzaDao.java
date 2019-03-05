package dominos.demo.model.daos;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.orders.Order;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.OrderRepository;
import dominos.demo.model.repositories.PizzaRepository;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidInputException;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import dominos.demo.model.enums.Size;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PizzaDao {

    private static final String UPDATE_QUANTITY = "UPDATE pizzas SET quantity= ? WHERE id = ?";

    private static final String EDIT_PIZZA = "UPDATE pizzas SET name = ?, description = ?, size = ?, weight = ?  , price = ?  , image_url = ? WHERE id = ?";

    public static final String SHOPPING_CART = "cart";
    public static final String USER = "user";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    TransactionTemplate transactionTemplate;

    public void addPizza(Pizza pizza){
        pizzaRepository.save(pizza);
    }

    public List<Pizza> getAllPizzas(){
        return pizzaRepository.findAll();
    }

    public List<Pizza> getPizzasByNameOrderedByPrice(String name){
        return pizzaRepository.findAllByNameOrderByPrice(name);
    }

    public List<Pizza> getAllPizzasByName(String name) {
        return pizzaRepository.findAllByName(name);
    }

    public List<Pizza> getPizzasPriceFilter(double price) { return pizzaRepository.findAllByPriceLessThan(price);}

    public Optional<Pizza> getById(Long aLong){
        return pizzaRepository.findById(aLong);
    }

    public Optional<Pizza> getByNameAndSize(String name, Size size){
        return pizzaRepository.findByNameAndSize(name, size);
    }

    public CommonResponseDTO deletePizzaById(long id) throws InvalidInputException {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if(pizza.isPresent()) {
            pizzaRepository.delete(pizza.get());
            return new CommonResponseDTO(pizza.get().getName()  + " was successfully deleted from database!", LocalDateTime.now());
        }
        throw new InvalidInputException("Pizza with id:" + id + " does not exist in database.");
    }
    public Pizza getProductById(long id){
        Optional<Pizza> ingredient = pizzaRepository.findById(id);
        if (ingredient.isPresent()) {
            return ingredient.get();
        }
        else {
            return null;
        }
    }
    //TODO dto for showing shopping cart ->view in dominos

    public void changePizzaQuantity(long id, int quantity) throws BaseException {
        if(checkIfPizzaExists(id)){
            jdbcTemplate.update(UPDATE_QUANTITY, quantity, id);
        }
    }

    public boolean checkIfPizzaExists(long id) throws ProductException {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if(pizza.isPresent()){
            return true;
        }
        throw new ProductException("The product does not exist! Please add it first!");
    }
}
