package dominos.demo.model.daos;

import dominos.demo.model.products.Ingredients;
import dominos.demo.model.products.Non_Pizza;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.products.Product;
import dominos.demo.model.repositories.ProductRepository;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Component
public class ProductDao {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addProduct(Product product){
        productRepository.save(product);
    }

    public List<Pizza> getAllPizzas() {
        return (List<Pizza>)(List<?>)productRepository.findAll();
    }

    public List<Non_Pizza> getAllNonPizzas() {
        return (List<Non_Pizza>)(List<?>)productRepository.findAll();
    }

    public List<Ingredients> getAllIngredients() {
        return (List<Ingredients>)(List<?>)productRepository.findAll();
    }

    public boolean checkIfProductExistsById(long id, String table) throws Exception {
        try (Connection con = jdbcTemplate.getDataSource().getConnection();) {
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM  "+table+" WHERE id = ?;");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int result = rs.getInt(1);
            // if result != 0, there are product with this id
            if (result != 0) {
                return true;
            }
            throw new ProductException("The product  does not exist! Please add it first!");
        }
    }
    public void changeProductQuantity(long id, int quantity, String table) throws Exception {
        if(checkIfProductExistsById(id, table)) {
            try (Connection c = jdbcTemplate.getDataSource().getConnection();) {
                PreparedStatement ps = c.prepareStatement("UPDATE "+ table +" SET quantity= ? WHERE id= ?");
                ps.setInt(1, quantity);
                ps.setLong(2, id);
                ps.execute();
            }
        }
    }
    public void deleteProductById(long id, String table) throws Exception {
        if(checkIfProductExistsById(id, table)) {
            try (Connection c = jdbcTemplate.getDataSource().getConnection();) {
                PreparedStatement ps = c.prepareStatement("DELETE FROM " + table+" WHERE id=?;");
                ps.setLong(1, id);
                ps.execute();
            }
        }
    }

    //TODO : add in DB quantity for pizzas non-pizzas and ingredients

}
