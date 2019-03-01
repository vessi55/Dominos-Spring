package dominos.demo.model.products;
import dominos.demo.model.enums.Size;
import java.util.Set;

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
    protected long id;
    protected String name;
    private String description;
    protected double price;
    private Size size;
    private double weight;
    private String image_url;
}
