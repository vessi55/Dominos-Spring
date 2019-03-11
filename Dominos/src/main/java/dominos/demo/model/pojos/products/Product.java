package dominos.demo.model.pojos.products;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

@Getter
@Setter
public abstract class Product {
    public abstract long getId();
    public abstract String getName();
    public abstract double getPrice();
    public abstract void insertIntoTable(JdbcTemplate jdbcTemplate, long productId, long order_id, int quantity, double price);
}
