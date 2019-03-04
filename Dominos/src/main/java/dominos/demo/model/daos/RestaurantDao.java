package dominos.demo.model.daos;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.PizzaRepository;
import dominos.demo.model.repositories.RestaurantRepository;
import dominos.demo.model.restaurants.Restaurant;
import dominos.demo.util.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class RestaurantDao {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addRestaurant(Restaurant restaurant){
        restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAll(){
        return restaurantRepository.findAll();
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
