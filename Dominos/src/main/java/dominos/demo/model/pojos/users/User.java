package dominos.demo.model.pojos.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dominos.demo.model.pojos.orders.Order;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    @Column(name = "is_admin")
    private boolean isAdmin;
    private String phone;
    @JsonIgnore
    @OneToMany(mappedBy = "user_id", orphanRemoval = true)
    private Set<Order> myOrders = new HashSet<>();
}
