package dominos.demo.model.daos;

import dominos.demo.controller.BaseController;
import dominos.demo.controller.SessionManager;
import dominos.demo.model.DTOs.OrderDto;
import dominos.demo.model.orders.Order;
import dominos.demo.model.products.Product;
import dominos.demo.model.repositories.OrderRepository;
import dominos.demo.model.users.Address;
import dominos.demo.model.users.User;
import dominos.demo.util.MailUtil;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDao {

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

    public OrderDto orderProductFromRestaurant(long restaurant_id, String delivery_time, HttpSession session) {
        HashMap<Product, Integer> shoppingCart = (HashMap<Product, Integer>) session.getAttribute(SessionManager.SHOPPING_CART);
        User user = (User) session.getAttribute(SessionManager.LOGGED);
        Order order = new Order();
        double total_sum = calculatePrice(shoppingCart);
        setValuesForOrder(order, convertStringToLDT(delivery_time), total_sum, user, restaurant_id);
        orderRepository.save(order);
        createCooker(order);
        saveRecordsIntoTable(shoppingCart, order);
        session.setAttribute(SessionManager.SHOPPING_CART, null);
        new Thread(()->{sendingMail(order,user);}).start();
        return new OrderDto(order.getTotal_sum(), order.getOrder_time(), order.getDelivery_time(), order.getStatus(),
                order.getDelivery_city(), order.getDelivery_street());
    }

    public OrderDto orderPizzaToAddress(long adrressId, HttpSession session) throws BaseException {
        HashMap<Product, Integer> shoppingCart = (HashMap<Product, Integer>) session.getAttribute(SessionManager.SHOPPING_CART);
        User user = (User) session.getAttribute(SessionManager.LOGGED);
        Order order = new Order();
        double total_sum = calculatePrice(shoppingCart);
        setValuesForOrder(order,LocalDateTime.now().plusMinutes(30), total_sum, user, null);
        checkIfAddressExistForUser(user, order,adrressId);
        orderRepository.save(order);
        createCooker(order);
        saveRecordsIntoTable(shoppingCart,order);
        session.setAttribute(SessionManager.SHOPPING_CART, null);
        new Thread(()->{sendingMail(order,user);}).start();
        return new OrderDto(order.getTotal_sum(), order.getOrder_time(), order.getDelivery_time(), order.getStatus(),
                order.getDelivery_city(), order.getDelivery_street());
    }

    public void checkIfAddressExistForUser(User user, Order order,long adrressId) throws InvalidAddressException {
        List<Address> addresses = addressDao.getAddressesByUserId(user.getId());
        for (Address a : addresses){
            if(a.getId() == adrressId){
                order.setDelivery_street(a.getStreet());
                order.setDelivery_city(a.getCity());
                System.out.println(order.getDelivery_city());
                System.out.println(order.getDelivery_street());
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

    public void setValuesForOrder(Order order,LocalDateTime time, double total_sum, User user, Long restaurant_id) {
        order.setTotal_sum(total_sum);
        order.setOrder_time(LocalDateTime.now());
        order.setDelivery_time(time);
        order.setStatus("Pending");
        order.setUser_id(user.getId());
        order.setRestaurant_id(restaurant_id);
    }

    public void saveRecordsIntoTable(HashMap<Product, Integer> shoppingCart, Order order) {
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            long productId = entry.getKey().getId();
            Integer quantity = entry.getValue();
            double price = entry.getKey().getPrice();
            entry.getKey().insertIntoTable(jdbcTemplate, productId, order.getId(), quantity, price);
        }
    }

    public void sendingMail(Order order, User user){
            try {
                MailUtil.sendMail("Dominos - Order",
                        "===== Order Information ===== " +
                                "\nOrder time : " + order.getOrder_time() +
                                "\nDelivery time : " + order.getDelivery_time() +
                                "\nSum : " + order.getTotal_sum() +  " lv.",
                        user.getEmail());
            } catch (Exception e) {
                BaseController.logger.error(e.getMessage());
            }
    }

    private LocalDateTime convertStringToLDT(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        return dateTime;
    }
}
