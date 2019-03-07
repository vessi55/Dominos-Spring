package dominos.demo.model.daos;

import dominos.demo.model.DTOs.ProductDTO;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.products.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String MY_ORDERS =
            "(SELECT order_time, name, price, orders.id \n" +
                    "FROM users RIGHT JOIN orders \n" +
                    "ON users.id = orders.user_id \n" +
                    "JOIN pizza_orders \n" +
                    "ON orders.id = pizza_orders.order_id \n" +
                    "JOIN pizzas \n" +
                    "ON pizzas.id = pizza_orders.pizza_id\n" +
                    "WHERE users.id = ?\n" +
                    "UNION\n" +
                    "SELECT order_time, name, price, orders.id \n" +
                    "FROM users RIGHT JOIN orders \n" +
                    "ON users.id = orders.user_id \n" +
                    "JOIN non_pizza_orders \n" +
                    "ON orders.id = non_pizza_orders.order_id \n" +
                    "JOIN non_pizzas \n" +
                    "ON non_pizzas.id = non_pizza_orders.non_pizza_id\n" +
                    "WHERE users.id = ? ) " +
                    "ORDER BY id";

    public List<ProductDTO> showMyOrders(long user_id) {
        List<ProductDTO> myorders = jdbcTemplate.query(MY_ORDERS, new Object[]{user_id, user_id}, new BeanPropertyRowMapper<>(ProductDTO.class));
        return myorders;

    }
}
