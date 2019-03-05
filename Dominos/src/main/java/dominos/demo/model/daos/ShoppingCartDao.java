package dominos.demo.model.daos;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.orders.Order;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.OrderRepository;
import dominos.demo.model.repositories.PizzaRepository;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Transactional(rollbackFor = BaseException.class)
@Component
public class ShoppingCartDao {

    public static final String SHOPPING_CART = "cart";
    public static final String USER = "user";

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    OrderRepository orderRepository;

    public CommonResponseDTO makeOrder(HttpSession session){
        HashMap<Pizza, Integer> shoppingCart = (HashMap<Pizza, Integer> )session.getAttribute(SHOPPING_CART);
        double price = calculatePrice(shoppingCart);
        User user = (User) session.getAttribute(USER);
        Order order = new Order();
        order.setTotal_sum(price);
        order.setOrder_time(LocalDateTime.now());
        order.setDelivery_time(LocalDateTime.now());
        order.setStatus("ready");
        order.setUser_id(user.getId());
        //order.setRestaurant_id();
        orderRepository.save(order);
        updateQuantity(shoppingCart);
        return new CommonResponseDTO("Successfull", LocalDateTime.now());
    }

    public double calculatePrice(HashMap<Pizza, Integer> products){
        double price = 0.0;
        for(Map.Entry<Pizza, Integer> e : products.entrySet()){
            double pizzaPrice = e.getKey().getPrice();
            int quantity = e.getValue();
            price += (pizzaPrice*quantity);
        }
        return price;
    }
    private void updateQuantity( HashMap<Pizza, Integer> products)  {
        for (Map.Entry<Pizza, Integer> e : products.entrySet()) {
            jdbcTemplate.update("UPDATE pizzas SET quantity = quantity - ? where id = ?", e.getValue(), e.getKey().getId());
        }
    }
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


}
