package dominos.demo.model.DTOs;

import dominos.demo.model.enums.Size;
import dominos.demo.model.products.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PizzaResponseDto {
    private String name;
    private String description;
    private Size size;
    private double weight;
    private double price;
}
