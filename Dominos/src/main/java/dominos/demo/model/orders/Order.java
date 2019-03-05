package dominos.demo.model.orders;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

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
    private String delivery_city;
    private String delivery_street;
    @Column(nullable = true)
    private Long restaurant_id;
    private long user_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                Double.compare(order.total_sum, total_sum) == 0 &&
                restaurant_id == order.restaurant_id &&
                user_id == order.user_id &&
                Objects.equals(order_time, order.order_time) &&
                Objects.equals(delivery_time, order.delivery_time) &&
                Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, total_sum, order_time, delivery_time, status, restaurant_id, user_id);
    }
}
