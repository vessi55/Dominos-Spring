package dominos.demo.model.DTOs;

import dominos.demo.model.products.NonPizza;
import dominos.demo.model.products.Pizza;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartViewDto {
    private String pizzaName;
    private int pizzaQuantity;
    private double pizzaPrice;
    // TODO shopping cart with non pizzas !!
//    private NonPizza nonPizza;
//    private int nonPizzaQuantity;
//    private double nonPizzaPrice;
}
