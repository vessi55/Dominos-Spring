package dominos.demo.model.products;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class Product {
    protected long id;
    protected String name;
    protected double price;
    //private String image;
}
