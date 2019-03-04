package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.PizzaDao;
import dominos.demo.model.daos.ShoppingCartDao;
import dominos.demo.model.products.Pizza;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.EmptyShoppingCartException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
public class ShoppingCartController extends BaseController{


    public static final String SHOPPING_CART = "cart";
    public static final String USER = "user";

    @Autowired
    PizzaDao pizzaDao;

    @Autowired
    ShoppingCartDao shoppingCartDao;

    @PostMapping(value = ("/pizzas/{id}/shoppingCart/add"))
    public CommonResponseDTO addPizzaToShoppingCart(@PathVariable("id") long id, HttpSession session)  throws InvalidLogInException {
        if(SessionManager.isLoggedIn(session)) { //TODO : exception for not logged in
            HashMap<Pizza, Integer> shoppingCart;
            Pizza pizza = pizzaDao.getById(id);
            if (session.getAttribute(SHOPPING_CART) == null) {
                session.setAttribute(SHOPPING_CART, new HashMap<Pizza, Integer>());
                shoppingCart = (HashMap<Pizza, Integer>) session.getAttribute(SHOPPING_CART);
                shoppingCart.put(pizza, 1);
            } else {
                shoppingCart = (HashMap<Pizza, Integer>) session.getAttribute("cart");
                if (!shoppingCart.containsKey(pizza)) {
                    shoppingCart.put(pizza, 1);
                }
                shoppingCart.put(pizza, shoppingCart.get(pizza) + 1);
            }
            return new CommonResponseDTO("Pizza " + pizza.getName() + " is successfully added to your shopping cart!", LocalDateTime.now());
        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }

    @PostMapping(value = ("/shoppingCart/order"))
    public CommonResponseDTO makeOrder(HttpSession session) throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            if (session.getAttribute(SHOPPING_CART) != null) {
                shoppingCartDao.makeOrder(session);
                return new CommonResponseDTO("Your order was successful!", LocalDateTime.now());
            }
            throw new EmptyShoppingCartException("Your shopping cart is empty!");
        }
        throw new InvalidLogInException("You are not logged! Please log in to continue!");
    }
//    @GetMapping(value = ("/shoppingCart"))
//    public ArrayList<ShoppingCartViewDto> viewCart(HttpSession session) throws BaseException{
//        if(SessionManager.isLoggedIn(session)) {
//            if (session.getAttribute(SHOPPING_CART) != null) {
//                return productDao.viewShoppingCart(session);
//            } else {
//                throw new EmptyShoppingCartException("Your shopping cart is empty!");
//            }
//        }
//        throw new InvalidLogInException("Please log in to continue");
//    }    @PostMapping(value = ("/products/{id}/shoppingCart/add"))

////    @GetMapping(value = ("/shoppingCart"))
////    public ArrayList<ShoppingCartViewDto> viewCart(HttpSession session) throws BaseException{
////        if(SessionManager.isLoggedIn(session)) {
////            if (session.getAttribute(SHOPPING_CART) != null) {
////                return productDao.viewShoppingCart(session);
////            } else {
////                throw new EmptyShoppingCartException("Your shopping cart is empty!");
////            }
////        }
////        throw new InvalidLogInException("Please log in to continue");
////    }

}
