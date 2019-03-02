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
@Table(name = "ingredients")

public class Ingredients extends Product{
    private ProductCategory category;
}
