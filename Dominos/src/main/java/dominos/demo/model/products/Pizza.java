package dominos.demo.model.products;
import dominos.demo.model.enums.Size;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;


@Getter
@Setter
@NoArgsConstructor
@Table(name = "pizza")
public class Pizza extends Product {
    private String description;
    private double weight;
    private Size size;
    private Set<Ingredients> ingredients;
    private String image_url;
}
