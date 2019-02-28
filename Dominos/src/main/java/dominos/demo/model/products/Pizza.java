package dominos.demo.model.products;
import dominos.demo.model.enums.Size;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Pizza extends Product {
    private String description;
    private double weight;
    private Size size;
    private Set<Ingredients> ingredients;
    private String image_url;


}
