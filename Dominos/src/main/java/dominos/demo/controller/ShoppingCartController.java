package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.NonPizzaDao;
import dominos.demo.model.DTOs.ShoppingCartViewDto;
import dominos.demo.model.daos.PizzaDao;
import dominos.demo.model.daos.ShoppingCartDao;
import dominos.demo.model.products.NonPizza;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.products.Product;
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



    @Autowired
    PizzaDao pizzaDao;

    @Autowired
    NonPizzaDao nonPizzaDao;

    @Autowired
    ShoppingCartDao shoppingCartDao;

    public HashMap<Product, Integer> shoppingCart;


    @PostMapping(value = ("/pizzas/{id}/quantity/{quantity}/shoppingCart/add"))
    public CommonResponseDTO addPizzaToShoppingCart(@PathVariable("id") long id, @PathVariable("quantity") int quantity,
                                                    HttpSession session) throws InvalidLogInException {
        if(SessionManager.isLoggedIn(session)) {
            Pizza pizza = pizzaDao.getProductById(id);
           this.addToShoppingCart(session, pizza,quantity);
            //TODO: fix message
            return new CommonResponseDTO("Pizza " + pizza.getName() + " - " + quantity + " is successfully " +
                    "added to your shopping cart!", LocalDateTime.now() );
        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }

    public HashMap<Product, Integer> addToShoppingCart(HttpSession session, Product product, int quantity){
        if (session.getAttribute(SessionManager.SHOPPING_CART) == null) {
            session.setAttribute(SessionManager.SHOPPING_CART, new HashMap<Product, Integer>());
        }
        shoppingCart = (HashMap<Product, Integer>) session.getAttribute(SessionManager.SHOPPING_CART);
        if (!shoppingCart.containsKey(product)) {
            shoppingCart.put(product, quantity);
        }
        else {
            shoppingCart.put(product, shoppingCart.get(product) + quantity);
        }
        return shoppingCart;
    }

    @PostMapping(value = ("/products/{id}/quantity/{quantity}/shoppingCart/add"))
    public CommonResponseDTO addProductToShoppingCart(@PathVariable("id") long id, @PathVariable("quantity") int quantity,
                                                    HttpSession session) throws InvalidLogInException {
        if(SessionManager.isLoggedIn(session)) {
            NonPizza product = nonPizzaDao.getProductById(id);
            this.addToShoppingCart(session, product,quantity);
            //TODO:fix message
            return new CommonResponseDTO("Product " + product.getName() + " - " + quantity + " is successfully " +
                    "added to your shopping cart!", LocalDateTime.now());
        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }

    @GetMapping(value = "/myShoppingCart")
    public List<ShoppingCartViewDto> getMyShoppingCart(HttpSession session) throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            if (session.getAttribute(SessionManager.SHOPPING_CART) != null) {
                return shoppingCartDao.viewShoppingCart(session);
            } else {
                throw new EmptyShoppingCartException("Your shopping cart is empty!");
            }
        }
        throw new InvalidLogInException("Please log in to continue");
    }
}
