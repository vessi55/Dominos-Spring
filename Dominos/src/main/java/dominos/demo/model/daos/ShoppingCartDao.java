package dominos.demo.model.daos;

import dominos.demo.controller.SessionManager;
import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.DTOs.ShoppingCartViewDto;
import dominos.demo.model.orders.Order;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.products.Product;
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
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Transactional(rollbackFor = BaseException.class)
@Component
public class ShoppingCartDao {

    public static final String SHOPPING_CART = "cart";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    OrderRepository orderRepository;


    private void updateQuantity(HashMap<Pizza, Integer> products)  {
        for (Map.Entry<Pizza, Integer> e : products.entrySet()) {
            jdbcTemplate.update("UPDATE pizzas SET quantity = quantity - ? where id = ?", e.getValue(), e.getKey().getId());
        }
    }

    public List<ShoppingCartViewDto> viewShoppingCart(HttpSession session) {
        LinkedList<ShoppingCartViewDto> products = new LinkedList<>();
        HashMap<Product, Integer> shoppingCart = (HashMap<Product, Integer>) session.getAttribute(SHOPPING_CART);
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            ShoppingCartViewDto shoppingCartViewDto = new ShoppingCartViewDto();
            shoppingCartViewDto.setPizzaName(entry.getKey().getName());
            shoppingCartViewDto.setPizzaQuantity(entry.getValue());
            shoppingCartViewDto.setPizzaPrice(entry.getKey().getPrice()*entry.getValue());
            products.add(shoppingCartViewDto);
        }
        return products;
    }



}
