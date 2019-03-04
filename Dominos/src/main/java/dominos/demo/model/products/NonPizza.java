package dominos.demo.model.products;
import dominos.demo.model.enums.NonPizzaCategory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "non_pizzas")
public class NonPizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NonPizzaCategory category;
    private double price;
    private double quantity;
    private String image_url;
}
