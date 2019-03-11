package dominos.demo.model.DTOs;

import dominos.demo.model.pojos.enums.Size;
import dominos.demo.model.pojos.products.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientResponseDto {
    private String pizzaName;
    private String pizzaDescription;
    private Size size;
    private double weight;
    private HashSet<Ingredient>  additionalIngredients;
}
