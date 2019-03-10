package dominos.demo.model.daos;

import dominos.demo.controller.SessionManager;
import dominos.demo.model.DTOs.IngredientResponseDto;
import dominos.demo.model.DTOs.ProductDTO;
import dominos.demo.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String MY_ORDERS =
            "(SELECT p.name, po.quantity, po.price, order_time, total_sum, orders.id \n" +
                    "FROM users RIGHT JOIN orders \n" +
                    "ON users.id = orders.user_id \n" +
                    "JOIN pizza_orders AS po \n" +
                    "ON orders.id = po.order_id\n" +
                    "JOIN pizzas AS p\n" +
                    "ON p.id = po.pizza_id\n" +
                    "WHERE users.id = ?\n" +
                    "UNION\n" +
                    "SELECT np.name, npo.quantity, npo.price, order_time, total_sum, orders.id \n" +
                    "FROM users RIGHT JOIN orders \n" +
                    "ON users.id = orders.user_id\n" +
                    "JOIN non_pizza_orders AS npo\n" +
                    "ON orders.id = npo.order_id\n" +
                    "JOIN non_pizzas AS np\n" +
                    "ON np.id = npo.non_pizza_id\n" +
                    "WHERE users.id = ?)\n" +
                    "ORDER BY id";

    private static final String STATS =
            "SELECT p.name, p.description,p.size,p.weight,p.price, i.name, i.price\n" +
                    "FROM pizzas as p \n" +
                    "JOIN pizza_ingredients\n" +
                    "ON p.id = pizza_ingredients.pizza_id\n" +
                    "JOIN ingredients AS i\n" +
                    "ON pizza_ingredients.ingredient_id = i.id;";

    public List<ProductDTO> showMyOrders(HttpSession session) {
        User user = (User) session.getAttribute(SessionManager.LOGGED);
        long user_id = user.getId();
        List<ProductDTO> myorders = jdbcTemplate.query(MY_ORDERS,
                new Object[]{user_id, user_id}, new BeanPropertyRowMapper<>(ProductDTO.class));
        return myorders;
    }
    public List<IngredientResponseDto> showMyFavOrders() {
        List<IngredientResponseDto> myorders = jdbcTemplate.query(STATS,
                new Object[]{}, new BeanPropertyRowMapper<>(IngredientResponseDto.class));
        return myorders;
    }

}
