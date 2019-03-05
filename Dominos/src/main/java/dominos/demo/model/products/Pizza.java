package dominos.demo.model.products;
import dominos.demo.model.enums.Size;

import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pizzas")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double price;private String description;
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
}
