package dominos.demo.model.daos;

import dominos.demo.controller.SessionManager;
import dominos.demo.model.DTOs.AddressResponseDTO;
import dominos.demo.model.DTOs.OrderDto;
import dominos.demo.model.orders.Order;
import dominos.demo.model.products.NonPizza;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.products.Product;
import dominos.demo.model.repositories.OrderRepository;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidAddressException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PizzaOrderDao {

    @Autowired
    AddressDao addressDao;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public double calculatePrice(HashMap<Product, Integer> products){
        double price = 0.0;
        for(Map.Entry<Product, Integer> e : products.entrySet()){
            double productPrice = e.getKey().getPrice();
            int quantity = e.getValue();
            price += (productPrice*quantity);
        }
        return price;
    }
    public void orderProductFromRestaurant(long restaurant_id, HttpSession session)  {
        HashMap<Product, Integer> shoppingCart = (HashMap<Product, Integer>)session.getAttribute(SessionManager.SHOPPING_CART);
        User user = (User) session.getAttribute(SessionManager.LOGGED);
        double total_sum = calculatePrice(shoppingCart);
        Order order = new Order();
        order.setTotal_sum(total_sum);
        order.setOrder_time(LocalDateTime.now());
        order.setDelivery_time(LocalDateTime.now()); // TODO: Find way to parse LocalDateTime to String
        order.setStatus("ready"); //TODO : Thread to changeStatus
        order.setUser_id(user.getId());
        order.setRestaurant_id(restaurant_id);
        orderRepository.save(order);
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            long productId = entry.getKey().getId();
            int quantity = entry.getValue();
            entry.getKey().insertIntoTable(jdbcTemplate,productId,order.getId(),quantity);
            SessionManager.SHOPPING_CART = null;
        }
    }

    public void orderPizzaToAddress(String city,String street, HttpSession session)throws BaseException {
        HashMap<Product, Integer> shoppingCart = (HashMap<Product, Integer>)session.getAttribute(SessionManager.SHOPPING_CART);
        User user = (User) session.getAttribute(SessionManager.LOGGED);
        List<AddressResponseDTO> addresses = addressDao.getAllUserAddresses(user.getId());
        double total_sum = calculatePrice(shoppingCart);
        Order order = new Order();
        order.setTotal_sum(total_sum);
        order.setOrder_time(LocalDateTime.now());
        order.setDelivery_time(LocalDateTime.now().plusMinutes(15));
        order.setStatus("ready");
        order.setUser_id(user.getId());
        order.setRestaurant_id(null);
        for(AddressResponseDTO a : addresses){
            if(a.getCity().equals(city) && a.getStreet().equals(street)){
                order.setDelivery_city(city);
                order.setDelivery_street(street);
            }
            else{
                throw new InvalidAddressException("Address does not exist! Please add it!");
            }
        }
        orderRepository.save(order);
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()){
            long pizzaId = entry.getKey().getId();
            int quantity = entry.getValue();
            jdbcTemplate.update("INSERT INTO pizza_orders (pizza_id, order_id, quantity) VALUES (?,?,?)",
                    pizzaId,order.getId(),quantity);
        }
    }


}
