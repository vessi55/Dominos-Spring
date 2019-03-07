package dominos.demo.model.daos;

import dominos.demo.controller.SessionManager;
import dominos.demo.model.DTOs.AddressResponseDTO;
import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.DTOs.OrderDto;
import dominos.demo.model.orders.Order;
import dominos.demo.model.products.Ingredient;
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
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
public class ProductOrderDao {

    @Autowired
    AddressDao addressDao;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public double calculatePrice(HashMap<Product, Integer> products) {
        double price = 0.0;
        for (Map.Entry<Product, Integer> e : products.entrySet()) {
            double productPrice = e.getKey().getPrice();
            int quantity = e.getValue();
            price += (productPrice * quantity);
        }
        return price;
    }

    public void orderProductFromRestaurant(long restaurant_id, HttpSession session) {
        HashMap<Product, Integer> shoppingCart = (HashMap<Product, Integer>) session.getAttribute(SessionManager.SHOPPING_CART);
        User user = (User) session.getAttribute(SessionManager.LOGGED);
        Order order = new Order();
        double total_sum = calculatePrice(shoppingCart);
        setValuesForOrder(order, total_sum, user, restaurant_id);
        orderRepository.save(order);
        saveRecordsIntoTable(shoppingCart, order);
        SessionManager.SHOPPING_CART = null;
    }

    public void orderPizzaToAddress(String city, String street, HttpSession session) throws BaseException {
        HashMap<Product, Integer> shoppingCart = (HashMap<Product, Integer>) session.getAttribute(SessionManager.SHOPPING_CART);
        User user = (User) session.getAttribute(SessionManager.LOGGED);
        Order order = new Order();
        double total_sum = calculatePrice(shoppingCart);
        setValuesForOrder(order, total_sum, user, null);
        checkIfAddressExistForUser(user, order, city, street);
        orderRepository.save(order);
        createCooker(order);
        saveRecordsIntoTable(shoppingCart,order);
    }

    public void checkIfAddressExistForUser(User user, Order order, String city, String street) throws InvalidAddressException {
        List<AddressResponseDTO> addresses = addressDao.getAllUserAddresses(user.getId());
        for (AddressResponseDTO a : addresses) {
            if (a.getCity().equals(city) && a.getStreet().equals(street)) {
                order.setDelivery_city(city);
                order.setDelivery_street(street);
            }
        }
        if (order.getDelivery_city() == null && order.getDelivery_street() == null) {
            throw new InvalidAddressException("Invalid address!");
        }
    }

    public void createCooker(Order order) {
        new Thread(() -> {
            try {
                Thread.sleep(20000);
                changeOrderStatus("Accepted", order.getId());
                Thread.sleep(20000);
                changeOrderStatus("Finished", order.getId());
            }catch (InterruptedException e) {
                System.out.println("Problem with your order! - " + e.getMessage());
            }
        }).start();
    }

    public void changeOrderStatus(String status, long id) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }

    public void setValuesForOrder(Order order, double total_sum, User user, Long restaurant_id) {
        order.setTotal_sum(total_sum);
        order.setOrder_time(LocalDateTime.now());
        order.setDelivery_time(LocalDateTime.now().plusMinutes(30));
        order.setStatus("Pending");
        order.setUser_id(user.getId());
        order.setRestaurant_id(restaurant_id);
    }

    public void saveRecordsIntoTable(HashMap<Product, Integer> shoppingCart, Order order) {
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            long productId = entry.getKey().getId();
            Integer quantity = entry.getValue();
            entry.getKey().insertIntoTable(jdbcTemplate, productId, order.getId(), quantity);
        }
    }
}
