package dominos.demo.model.daos;

import dominos.demo.model.DTOs.ShoppingCartViewDto;
import dominos.demo.model.pojos.products.Pizza;
import dominos.demo.model.pojos.products.Product;
import dominos.demo.model.repositories.OrderRepository;
import dominos.demo.model.repositories.PizzaRepository;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.EmptyShoppingCartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
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

    public List<ShoppingCartViewDto> viewShoppingCart(HttpSession session) throws EmptyShoppingCartException {
        LinkedList<ShoppingCartViewDto> products = new LinkedList<>();
        HashMap<Product, Integer> shoppingCart = (HashMap<Product, Integer>) session.getAttribute(SHOPPING_CART);
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            ShoppingCartViewDto shoppingCartViewDto = new ShoppingCartViewDto();
            shoppingCartViewDto.setName(entry.getKey().getName());
            shoppingCartViewDto.setQuantity(entry.getValue());
            shoppingCartViewDto.setPrice(entry.getKey().getPrice()*entry.getValue());
            products.add(shoppingCartViewDto);
        }
        if(shoppingCart.isEmpty()) {
            throw new EmptyShoppingCartException("Your shopping cart is empty!");
        }
        return products;
    }



}
