package dominos.demo.model.products;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "id")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Non_Pizza.class, name = "non-pizza"),
        @JsonSubTypes.Type(value = Ingredients.class, name = "ingredients")
})
public abstract class Product  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    protected String name;
    protected double price;
}
