package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.NonPizzaDao;
import dominos.demo.model.DTOs.ShoppingCartViewDto;
import dominos.demo.model.daos.PizzaDao;
import dominos.demo.model.daos.ShoppingCartDao;
import dominos.demo.model.products.Pizza;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.EmptyShoppingCartException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
public class ShoppingCartController extends BaseController{


    public static final String SHOPPING_CART = "cart";

    @Autowired
    PizzaDao pizzaDao;

    @Autowired
    NonPizzaDao nonPizzaDao;

    @Autowired
    ShoppingCartDao shoppingCartDao;


    @PostMapping(value = ("/pizzas/{id}/quantity/{quantity}/shoppingCart/add"))
    public CommonResponseDTO addPizzaToShoppingCart(@PathVariable("id") long id, @PathVariable("quantity") int quantity,
                                                    HttpSession session) throws InvalidLogInException {
        if(SessionManager.isLoggedIn(session)) {
            HashMap<Pizza, Integer> shoppingCart;
            Pizza pizza = pizzaDao.getProductById(id);
            if (session.getAttribute(SHOPPING_CART) == null) {
                session.setAttribute(SHOPPING_CART, new HashMap<Pizza, Integer>());
            }
            shoppingCart = (HashMap<Pizza, Integer>) session.getAttribute(SHOPPING_CART);
            if (!shoppingCart.containsKey(pizza)) {
                shoppingCart.put(pizza, quantity);
            }
            else {
                shoppingCart.put(pizza, shoppingCart.get(pizza) + quantity);
            }
            return new CommonResponseDTO("Pizza " + pizza.getName() + " is successfully " +
                    "added to your shopping cart!", LocalDateTime.now());
        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }

    @GetMapping(value = "/myShoppingCart")
    public List<ShoppingCartViewDto> getMyShoppingCart(HttpSession session) throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            if (session.getAttribute(SHOPPING_CART) != null) {
                return shoppingCartDao.viewShoppingCart(session);
            } else {
                throw new EmptyShoppingCartException("Your shopping cart is empty!");
            }
        }
        throw new InvalidLogInException("Please log in to continue");
    }
}
