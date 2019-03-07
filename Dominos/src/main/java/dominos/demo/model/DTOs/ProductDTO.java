package dominos.demo.model.DTOs;

import dominos.demo.model.enums.NonPizzaCategory;
import dominos.demo.model.enums.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String name;
    private double price;
    private LocalDateTime order_time;
}
