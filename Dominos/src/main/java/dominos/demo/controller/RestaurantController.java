package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.DTOs.RestaurantDto;
import dominos.demo.model.daos.RestaurantDao;
import dominos.demo.model.pojos.restaurants.Restaurant;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidInputException;
import dominos.demo.util.exceptions.InvalidLogInException;
import dominos.demo.util.exceptions.ProductException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class RestaurantController extends BaseController {

    @Autowired
    RestaurantDao restaurantDao;

    @PostMapping(value = "/restaurants/add")
    public CommonResponseDTO saveRestaurant(@RequestBody Restaurant restaurant, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        validateRestaurantInput(restaurant);
        restaurantDao.addRestaurant(restaurant);
        return new CommonResponseDTO("Dominos: " + restaurant.getCity() + "-" + restaurant.getAddress()
                + " is successfully added", LocalDateTime.now());
    }

    @GetMapping(value = "/restaurants/all")
    public List<RestaurantDto> viewAllRestaurants(HttpSession session) throws InvalidLogInException {
        if(SessionManager.isLoggedIn(session)) {
            return restaurantDao.showAllRestaurants();
        }
        throw new InvalidLogInException("Please log in to view all restaurants!");
    }

    @GetMapping(value = "/restaurants/orders")
    public List<Restaurant> getAllRestaurants(HttpSession session) throws InvalidLogInException{
        SessionManager.validateLoginAdmin(session);
        return restaurantDao.getAll();
    }

    @GetMapping(value = "/restaurantsInCity")
    public List<RestaurantDto> viewAllRestaurantsInCity(@RequestParam("city") String city, HttpSession session)
                                                            throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            return restaurantDao.showAllInCity(city);
        }
        throw new InvalidLogInException("Please log in to view all restaurants!");
    }
    @GetMapping(value = "/restaurants/city/orders")
    public List<Restaurant> getAllRestaurantsInCity(@RequestParam("city") String city, HttpSession session) throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            List<Restaurant> restaurants = restaurantDao.getAllInCity(city);
            if (restaurants.isEmpty()) {
                throw new InvalidInputException("No restaurants in city: " + city);
            }
            return restaurants;
        }
        else {
            throw new InvalidLogInException("Please log in to view all restaurants!");
        }
    }

    @GetMapping(value = "/restaurants/id")
    public Optional<Restaurant> getRestaurantById(@RequestParam("id") Long id, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        Optional<Restaurant> restaurant = restaurantDao.getById(id);
        if (restaurant.isPresent()) {
            return restaurant;
        } else {
            throw new ProductException("Restaurant with id:" + id + " does not exist in database!");
        }
    }

    @GetMapping(value = "/restaurants/address")
    public Optional<Restaurant> getRestaurantByAddress(@RequestParam("city") String city,
                                                       @RequestParam("address") String address,
                                                       HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        Optional<Restaurant> restaurant = restaurantDao.getByCityAndAddress(city, address);
        if (restaurant.isPresent()) {
            return restaurant;
        } else {
            throw new ProductException("Restaurant with address: " + address + " does not exist in database!");
        }
    }

    @DeleteMapping(value = ("/restaurants/{id}/delete"))
    public CommonResponseDTO deleteRestaurant(@PathVariable("id") long id, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        restaurantDao.deleteRestaurantById(id);
        return new CommonResponseDTO("Restaurant with id: " + id + " was removed! : ", LocalDateTime.now());
    }

    private void validateRestaurantInput(Restaurant restaurant)throws InvalidInputException {
        if(restaurant.getCity().isEmpty() || restaurant.getAddress().isEmpty()){
            throw new InvalidInputException("Invalid input for the restaurant. Please try again! All fields must be filled in!");
        }
    }
}

