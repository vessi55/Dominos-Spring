package dominos.demo.model.products;
import dominos.demo.model.enums.ProductCategory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "non-pizza")
public class Non_Pizza extends Product {
    private ProductCategory category;
    private int quantity;
    private String image_url;
}
