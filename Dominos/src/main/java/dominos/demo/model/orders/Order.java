package dominos.demo.model.orders;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dominos.demo.model.restaurants.Restaurant;
import dominos.demo.model.users.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double total_sum;
    private LocalDateTime order_time;
    private LocalDateTime delivery_time;
    private String status;
    private long restaurant_id;
    private long user_id;
}
