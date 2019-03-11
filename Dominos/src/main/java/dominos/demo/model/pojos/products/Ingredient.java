package dominos.demo.model.pojos.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dominos.demo.model.pojos.enums.IngredientCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ingredients")

public class Ingredient{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "category",length = 20)
    private IngredientCategory ingredientCategory;
    private double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(name, that.name) &&
                ingredientCategory == that.ingredientCategory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ingredientCategory, price);
    }

//    @Override
//    public void insertIntoTable(JdbcTemplate jdbcTemplate, long productId, long ingredientId, int quantity) {
//        jdbcTemplate.update("INSERT INTO pizza_ingredients (pizza_id, ingredient_id, quantity) VALUES (?,?,?)",
//                productId, ingredientId, quantity);
//    }
}


