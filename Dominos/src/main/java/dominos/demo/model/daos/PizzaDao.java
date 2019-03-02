package dominos.demo.model.daos;

import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.PizzaRepository;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Component
public class PizzaDao {

    public static final String PRODUCT_QUANTITY ="SELECT COUNT(*) FROM products WHERE id = ?";
    public static final String UPDATE_PRODUCT_QUANTITY = "UPDATE products SET quantity= ? WHERE id= ?";
    public static final String DELETE_PRODUCT_BY_ID = "DELETE FROM products WHERE id=?";
    public static final String GET_PRODUCT_ORDER_BY_PRICE_ASC = "SELECT id, name, description,size, weight, price, quantity, image_url FROM products ORDER BY price DESC";
    public static final String GET_PRODUCT_ORDER_BY_PRICE_DESC = "SELECT id, name, description,size, weight, price, quantity, image_url FROM products ORDER BY price ASC";
    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addPizza(Pizza pizza){
        pizzaRepository.save(pizza);
    }
    public List<Pizza> getAllPizas(){
       return pizzaRepository.findAll();
    }
    public Optional<Pizza> findPizzaByName(String name){
        return pizzaRepository.findByName(name);
    }

    private boolean checkIfPizzaExists(long id) throws Exception {
        try (Connection con = jdbcTemplate.getDataSource().getConnection();) {
            PreparedStatement ps = con.prepareStatement(PRODUCT_QUANTITY);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int result = rs.getInt(1);
            // if result != 0, there are product with this id
            if (result != 0) {
                return true;
            }
            throw new ProductException("This product does not exist! Please add it first!");
        }
    }
    public void changePizzaQuantity(long id, int quantity) throws Exception {
        if(checkIfPizzaExists(id)) {
            try (Connection c = jdbcTemplate.getDataSource().getConnection();) {
                PreparedStatement ps = c.prepareStatement(UPDATE_PRODUCT_QUANTITY);
                ps.setInt(1, quantity);
                ps.setLong(2, id);
                ps.execute();
            }
        }
    }
    public void deletePizza(long id) throws Exception {
        if(checkIfPizzaExists(id)) {
            try (Connection c = jdbcTemplate.getDataSource().getConnection();) {
                PreparedStatement ps = c.prepareStatement(DELETE_PRODUCT_BY_ID);
                ps.setLong(1, id);
                ps.execute();
            }
        }
    }
    //TODO : add in DB quantity for pizzas non-pizzas and ingredients

}
