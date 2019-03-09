package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.DTOs.ProductDTO;
import dominos.demo.model.daos.OrderDao;
import dominos.demo.model.daos.ProductDao;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class OrderController extends BaseController {

    @Autowired
    OrderDao orderDao;

    @Autowired
    ProductDao productDao;

    @PostMapping(value ="/order/restaurants/id")
    public CommonResponseDTO finishOrderForPizzaFromRestaurant(@RequestParam ("id") Long restaurant_id,
                                                               @RequestParam ("delivery_time") String delTime,
                                                               HttpSession session)
    throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            orderDao.orderProductFromRestaurant(restaurant_id,delTime, session);
            return new CommonResponseDTO("Successfull order!", LocalDateTime.now());
        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }


    @PostMapping(value ="/order/address/city/street")
    public CommonResponseDTO finishOrderForPizzaToAddress(@RequestParam  String city,
                                                               @RequestParam String street,
                                                               HttpSession session)
            throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            orderDao.orderPizzaToAddress(city, street, session);
            return new CommonResponseDTO("Successfull order! .. ", LocalDateTime.now()); //TODO
        }
        throw new InvalidLogInException("You are not logged in! Please log in to continue!");
    }

    @GetMapping(value = "/myOrders/user/{id}")
    public List<ProductDTO> showMyOrders(@PathVariable ("id") long id, HttpSession session) throws InvalidLogInException {
        if(SessionManager.isLoggedIn(session)) {
            return productDao.showMyOrders(id);
        }
        throw new InvalidLogInException("Please log in to view all your orders!");
    }

}
