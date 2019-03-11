package dominos.demo.controller;

import dominos.demo.model.DTOs.OrderDto;
import dominos.demo.model.DTOs.ProductDTO;
import dominos.demo.model.daos.OrderDao;
import dominos.demo.model.daos.ProductDao;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class OrderController extends BaseController {

    @Autowired
    OrderDao orderDao;

    @Autowired
    ProductDao productDao;

    @PostMapping(value ="/order/restaurants/id")
    public OrderDto finishOrderForPizzaFromRestaurant(@RequestParam ("id") Long restaurant_id,
                                                               @RequestParam ("delivery_time") String delTime,
                                                               HttpSession session)
    throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            return orderDao.orderProductFromRestaurant(restaurant_id,delTime, session);

        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }


    @PostMapping(value ="/order/addresses/{id}")
    public OrderDto finishOrderForPizzaToAddress(@PathVariable("id") long id, HttpSession session)
            throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            return orderDao.orderPizzaToAddress(id, session);
        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }

    @GetMapping(value = "/myOrders")
    public List<ProductDTO> showMyOrders( HttpSession session) throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            return productDao.showMyOrders(session);
        }
        throw new InvalidLogInException("Please log in to view all your orders!");
    }

}
