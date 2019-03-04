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
    private PlatformTransactionManager transactionManager;

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

    public void makeOrder(HttpSession session){
        HashMap<Pizza, Integer> shoppingCart = (HashMap<Pizza, Integer> )session.getAttribute(SHOPPING_CART);
        //first check product quantity (method can throw an exception)
        //maybe we can use a transaction -> get from catalogue and set it to user then quantity-1
        //traverse the shopping cart and calculate price
        double price = 0.0;
        for(Map.Entry<Pizza, Integer> e : shoppingCart.entrySet()){
            double pizzaPrice = e.getKey().getPrice();
            int quantity = e.getValue();
            price += (pizzaPrice*quantity);
        }
        //get user from session
        //someone we need to set attribute to session --->> user
        User user = (User) session.getAttribute(USER);
        //create object Order andwe need to set values
        Order order = new Order();
        order.setTotal_sum(price);
        order.setOrder_time(LocalDateTime.now());
        order.setDelivery_time(LocalDateTime.now());
        order.setStatus("ready");
        order.setUser(user);
        //order.setRestaurant();
        //save to table orders
        //we need to save all orders for every client --> need new table for this
        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                orderRepository.save(order);
                updateQuantity(shoppingCart);
                return null;
            }
        });
    }
    private void updateQuantity( HashMap<Pizza, Integer> products)  {
        for (Map.Entry<Pizza, Integer> e : products.entrySet()) {
            jdbcTemplate.update("UPDATE pizzas SET quantity = quantity - ? where id = ?", e.getValue(), e.getKey().getId());
        }
    }
    //TODO dto for showing shopping cart ->view in dominos
    //TODO : when add to db quantity make checker method for it

//    public Stack<ShoppingCartViewDto> viewShoppingCart(HttpSession session, Order order){
//        HashMap<Pizza, Integer> productsInCart = (HashMap<Pizza, Integer>)session.getAttribute(SHOPPING_CART);
//        Stack<ShoppingCartViewDto> products= new Stack<ShoppingCartViewDto>();
//        for(Map.Entry<Pizza, Integer> e : productsInCart.entrySet()){
//            Pizza pizza = e.getKey();
//            ShoppingCartViewDto shoppingCartViewDto = new ShoppingCartViewDto();
//            shoppingCartViewDto.setTotal_sum(pizza.getPrice() * e.getValue());
//            shoppingCartViewDto.setStatus(order.getStatus());
//            shoppingCartViewDto.setRestaurant(order.getRestaurant());
//            shoppingCartViewDto.setUser(order.getUser());
//            products.add(shoppingCartViewDto);
//        }
//        return products;
//
//    }

    //TODO : add in DB quantity for pizzas non-pizzas and ingredients


}
