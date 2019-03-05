package dominos.demo.model.repositories;

import dominos.demo.model.products.Pizza;
import dominos.demo.model.restaurants.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

    @Override
    List<Restaurant> findAll();

    List<Restaurant> findAllByCity(String city);

    @Override
    Optional<Restaurant> findById(Long aLong);

    Optional<Restaurant> findByCityAndAddress(String city, String address);

    @Override
    void delete(Restaurant restaurant);
}
