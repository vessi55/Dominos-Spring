package dominos.demo.model.products;
import dominos.demo.model.enums.Size;

import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pizzas")
public class Pizza extends Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private double price;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Size size;
    private double weight;
    private String image_url;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pizza pizza = (Pizza) o;
        return id == pizza.id &&
                Double.compare(pizza.price, price) == 0 &&
                Double.compare(pizza.weight, weight) == 0 &&
                Objects.equals(name, pizza.name) &&
                Objects.equals(description, pizza.description) &&
                size == pizza.size &&
                Objects.equals(image_url, pizza.image_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, description, size, weight, image_url);
    }

    @Override
    public void insertIntoTable(JdbcTemplate jdbcTemplate, long productId, long order_id, int quantity) {
        jdbcTemplate.update("INSERT INTO pizza_orders (pizza_id, order_id, quantity) VALUES (?,?,?)",
                productId, order_id, quantity);
    }
}
