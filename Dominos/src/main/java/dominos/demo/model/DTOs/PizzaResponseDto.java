package dominos.demo.model.DTOs;

import dominos.demo.model.pojos.enums.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
