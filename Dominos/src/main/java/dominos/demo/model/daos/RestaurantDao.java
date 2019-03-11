package dominos.demo.model.daos;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.DTOs.RestaurantDto;
import dominos.demo.model.pojos.users.Address;
import dominos.demo.model.repositories.RestaurantRepository;
import dominos.demo.model.pojos.restaurants.Restaurant;
import dominos.demo.util.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RestaurantDao {

    public static final String ALL_RESTAURANTS = "SELECT city, address FROM restaurants";
    public static final String ALL_RESTAURANTS_IN_CITY = "SELECT city, address FROM restaurants WHERE city = ?";
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addRestaurant(Restaurant restaurant){
        restaurantRepository.save(restaurant);
    }

    public List<RestaurantDto> showAllRestaurants() {
        List<RestaurantDto> restaurants  = jdbcTemplate.query(ALL_RESTAURANTS, new BeanPropertyRowMapper(RestaurantDto.class));
        return restaurants;
    }

    public List<Restaurant> getAll(){
        return restaurantRepository.findAll();
    }

    public List<RestaurantDto> showAllInCity(String city) throws InvalidInputException {
        List<RestaurantDto> restaurants =  jdbcTemplate.query(ALL_RESTAURANTS_IN_CITY, new Object[]{city},
                new BeanPropertyRowMapper<>(RestaurantDto.class));
        if(restaurants.isEmpty()) {
            throw new InvalidInputException("No restaurants in city: " + city);
        }
        return restaurants;
    }

    public List<Restaurant> getAllInCity(String city) {
        return restaurantRepository.findAllByCity(city);
    }

    public Optional<Restaurant> getById(Long aLong){
        return restaurantRepository.findById(aLong);
    }

    public Optional<Restaurant> getByCityAndAddress(String city, String address){
        return restaurantRepository.findByCityAndAddress(city, address);
    }

    public CommonResponseDTO deleteRestaurantById(long id) throws InvalidInputException {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if(restaurant.isPresent()) {
            restaurantRepository.delete(restaurant.get());
            return new CommonResponseDTO("Domino's: " +restaurant.get().getCity()+" - "+restaurant.get().getAddress()
                    + " was successfully deleted from database!", LocalDateTime.now());
        }
        throw new InvalidInputException("The restaurant does not exist in  database.");
    }
}
