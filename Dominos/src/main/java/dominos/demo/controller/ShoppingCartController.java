package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.IngredientDao;
import dominos.demo.model.daos.NonPizzaDao;
import dominos.demo.model.DTOs.ShoppingCartViewDto;
import dominos.demo.model.daos.PizzaDao;
import dominos.demo.model.daos.ShoppingCartDao;
import dominos.demo.model.products.Ingredient;
import dominos.demo.model.products.NonPizza;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.products.Product;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.EmptyShoppingCartException;
import dominos.demo.util.exceptions.InvalidLogInException;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@RestController
public class ShoppingCartController extends BaseController{

    @Autowired
    PizzaDao pizzaDao;

    @Autowired
    NonPizzaDao nonPizzaDao;

    @Autowired
    IngredientDao ingredientDao;

    @Autowired
    ShoppingCartDao shoppingCartDao;

    public HashMap<Product, Integer> shoppingCart;


    public void addProductToShoppingCart(HttpSession session, Product product, Integer quantity) {
        if (session.getAttribute(SessionManager.SHOPPING_CART) == null) {
            session.setAttribute(SessionManager.SHOPPING_CART, new HashMap<Product, Integer>());
        }
        shoppingCart = (HashMap<Product, Integer>) session.getAttribute(SessionManager.SHOPPING_CART);

        if (!shoppingCart.containsKey(product)) {
            shoppingCart.put(product, quantity);
        } else {
            shoppingCart.put(product, shoppingCart.get(product) + quantity);
        }
    }

    @PostMapping(value = ("/pizzas/quantity/{quantity}/shoppingCart/add"))
    public CommonResponseDTO addPizzaToShoppingCart(@PathVariable("quantity") Integer quantity,
                                                    HttpSession session) throws BaseException {
        Pizza pizza = (Pizza)session.getAttribute(SessionManager.PIZZA);
        HashMap<Pizza, HashSet<Ingredient>> pizzaExtras =
                (HashMap<Pizza, HashSet<Ingredient>>)session.getAttribute(SessionManager.PIZZA_INGREDIENTS);
        if(SessionManager.isLoggedIn(session)) {
            pizza.setPrice(pizzaDao.calculatePizzaPrice(pizzaExtras));
            this.addProductToShoppingCart(session, pizza, quantity);
            return new CommonResponseDTO("Pizza " + pizza.getName() + " with quantity "
                    + quantity + " is successfully " +
                    "added to your shopping cart!", LocalDateTime.now() );
        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }

    @PostMapping(value = ("/products/{id}/quantity/{quantity}/shoppingCart/add"))
    public CommonResponseDTO addProductToShoppingCart(@PathVariable("id") long id, @PathVariable("quantity") Integer quantity,
                                                    HttpSession session) throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            NonPizza nonPizza = nonPizzaDao.getProductById(id);
            if(nonPizza == null) {
                throw new ProductException("Product with id:" +id + " does not exist in database!");
            }
            this.addProductToShoppingCart(session, nonPizza,quantity);
            return new CommonResponseDTO("Product " + nonPizza.getName() + " - " + quantity + " is successfully " +
                    "added to your shopping cart!", LocalDateTime.now());
        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }


    public void removeFromShoppingCart(Product product, int quantity, HttpSession session) throws BaseException{
        shoppingCart = (HashMap<Product, Integer>) session.getAttribute(SessionManager.SHOPPING_CART);
        if (!shoppingCart.isEmpty()) {
            if (shoppingCart.containsKey(product)) {
                if(shoppingCart.get(product) == 1) {
                    shoppingCart.remove(product);
                }
                shoppingCart.put(product, shoppingCart.get(product) - quantity);
            }
        }
        else {
            throw new EmptyShoppingCartException("Your shopping cart is empty!");
        }
    }

    @DeleteMapping(value = ("/pizzas/{id}/quantity/{quantity}/shoppingCart/remove"))
    public CommonResponseDTO removePizzaFromShoppingCart(@PathVariable("id") long id, @PathVariable("quantity") Integer quantity,
                                                    HttpSession session) throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            Pizza pizza = pizzaDao.getProductById(id);
            this.removeFromShoppingCart(pizza, quantity, session);
            return new CommonResponseDTO("Pizza " + pizza.getName() + " - " + quantity + " is successfully " +
                    "removed from your shopping cart!", LocalDateTime.now());
        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }

    @DeleteMapping(value = ("/products/{id}/quantity/{quantity}/shoppingCart/remove"))
    public CommonResponseDTO removeProductFromShoppingCart(@PathVariable("id") long id, @PathVariable("quantity") Integer quantity,
                                                         HttpSession session) throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            NonPizza nonPizza = nonPizzaDao.getProductById(id);
            this.removeFromShoppingCart(nonPizza, quantity, session);
            return new CommonResponseDTO("Product " + nonPizza.getName() + " - " + quantity + " is successfully " +
                    "removed from your shopping cart!", LocalDateTime.now());
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
