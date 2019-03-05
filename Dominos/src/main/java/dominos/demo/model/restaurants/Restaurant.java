package dominos.demo.model.restaurants;

import dominos.demo.model.orders.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String address;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "restaurant_id", orphanRemoval = true)
    private Set<Order> myOrders = new HashSet<>();



}
