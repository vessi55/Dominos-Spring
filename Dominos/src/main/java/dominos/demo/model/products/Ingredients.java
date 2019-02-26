package dominos.demo.model.products;
import dominos.demo.model.enums.ProductCategory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Ingredients extends Product{
    private ProductCategory category;
}
