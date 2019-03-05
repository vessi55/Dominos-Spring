package dominos.demo.model.products;

import dominos.demo.model.enums.IngredientCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ingredients")

public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "category",length = 20)
    private IngredientCategory ingredientCategory;
    private double price;
}


