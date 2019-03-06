package dominos.demo.model.products;
import dominos.demo.model.enums.NonPizzaCategory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "non_pizzas")
public class NonPizza extends Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NonPizzaCategory category;
    private double price;
    private double measure;
    private String image_url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NonPizza nonPizza = (NonPizza) o;
        return id == nonPizza.id &&
                Double.compare(nonPizza.price, price) == 0 &&
                Double.compare(nonPizza.measure, measure) == 0 &&
                Objects.equals(name, nonPizza.name) &&
                category == nonPizza.category &&
                Objects.equals(image_url, nonPizza.image_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, price, measure, image_url);
    }
    @Override
    public void insertIntoTable(JdbcTemplate jdbcTemplate, long productId, long order_id, int quantity) {
        jdbcTemplate.update("INSERT INTO non_pizza_orders (non_pizza_id, order_id, quantity) VALUES (?,?,?)",
                productId, order_id, quantity);
    }
}


