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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class PizzaDao {

    private static final String UPDATE_QUANTITY = "UPDATE pizzas SET quantity= ? WHERE id = ?";

    private static final String EDIT_PIZZA = "UPDATE pizzas SET name = ?, description = ?, size = ?, weight = ?  , price = ?  , image_url = ? WHERE id = ?";

    public static final String SHOPPING_CART = "cart";
    public static final String USER = "user";

    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    OrderRepository orderRepository;

    public void addProduct(Pizza pizza){
        pizzaRepository.save(pizza);
    }

    public List<Pizza> getAllPizzas(){
        return pizzaRepository.findAll();
    }

    public List<Pizza> getAllPizzaNamesOrderedByPrice(String name){
        return pizzaRepository.findAllByNameOrderByPrice(name);
    }

    public List<Pizza> getAllPizzasByName(String name) {
        return pizzaRepository.findAllByName(name);
    }

    public Pizza getById(long id){
        Optional<Pizza> product = pizzaRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        }
        else {
            return null;
        }
    }

    public Pizza getByName(String name) {
        Optional<Pizza> product = pizzaRepository.findByName(name);
        if (product.isPresent()) {
            return product.get();
        }
        else {
            return null;
        }
    }

    public boolean checkIfProductExist(long id) throws ProductException {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if(pizza.isPresent()){
            return true;
        }
        throw new ProductException("The product does not exist! Please add it first!");
    }

    public void changePizzaQuantity(long id, int quantity) throws BaseException {
        if(checkIfProductExist(id)){
            jdbcTemplate.update(UPDATE_QUANTITY, quantity, id);
        }
    }

    public CommonResponseDTO deleteProductById(long id) throws InvalidInputException {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if(pizza.isPresent()) {
            pizzaRepository.delete(pizza.get());
            return new CommonResponseDTO(pizza.get().getName()  + " was successfully deleted from database!", LocalDateTime.now());
        }
        throw new InvalidInputException("The pizza does not exist in  database.");

    }


    //TODO dto for showing shopping cart ->view in dominos
    //TODO : when add to db quantity make checker method for it

    //TODO : add in DB quantity for pizzas non-pizzas and ingredients


}
