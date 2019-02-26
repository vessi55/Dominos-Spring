package dominos.demo.model.orders;

import java.time.LocalDateTime;
import java.util.Map;
import dominos.demo.model.products.Product;
import dominos.demo.model.restaurants.Restaurant;
import dominos.demo.model.users.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Order {

    private long id;
    private double total_sum;
    private LocalDateTime order_time;
    private LocalDateTime delivery_time;
    private String status; // TODO : could be enum or private boolean isDelivered
    private User user;
    private Map<Product, Integer> products; // product ->quantity
    private Restaurant restaurant;
    private float price;


}
