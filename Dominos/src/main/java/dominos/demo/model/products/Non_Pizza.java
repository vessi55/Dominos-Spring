package dominos.demo.model.products;
import dominos.demo.model.enums.ProductCategory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "non-pizzas")

public class Non_Pizza  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    protected String name;
    private ProductCategory category;
    protected double price;
    private int quantity;
    private String image_url;
}
