package dominos.demo.model.daos;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.enums.NonPizzaCategory;
import dominos.demo.model.products.NonPizza;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.NonPizzaRepository;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidInputException;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class NonPizzaDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NonPizzaRepository nonPizzaRepository;

    private static final String UPDATE_QUANTITY = "UPDATE non_pizzas SET quantity = ? WHERE id = ?";


    public void addNonPizza(NonPizza nonPizza){
        nonPizzaRepository.save(nonPizza);
    }

    public List<NonPizza> getAllNonPizzas(){
        return nonPizzaRepository.findAll();
    }

    public List<NonPizza> getAllByCategory(NonPizzaCategory category) { return nonPizzaRepository.findAllByCategory(category);}

    public List<NonPizza> getAllByCategoryOrderByPrice(NonPizzaCategory category){
        return nonPizzaRepository.findAllByCategoryOrderByPrice(category);
    }

    public List<NonPizza> getNonPizzasPriceFilter(double price) { return nonPizzaRepository.findAllByPriceLessThan(price);}

    public Optional<NonPizza> getById(Long aLong){
        return nonPizzaRepository.findById(aLong);
    }

    public Optional<NonPizza> getByName(String name){
        return nonPizzaRepository.findByName(name);
    }

    public CommonResponseDTO deleteNonPizzaById(long id) throws InvalidInputException {
        Optional<NonPizza> nonPizza = nonPizzaRepository.findById(id);
        if(nonPizza.isPresent()) {
            nonPizzaRepository.delete(nonPizza.get());
            return new CommonResponseDTO(nonPizza.get().getName()  + " was successfully deleted from database!", LocalDateTime.now());
        }
        throw new InvalidInputException("Product with id:" + id + " does not exist in database.");

    }

    public void changeProductQuantity(long id, int quantity) throws BaseException {
        if(checkIfProductExists(id)){
            jdbcTemplate.update(UPDATE_QUANTITY, quantity, id);
        }
    }

    public boolean checkIfProductExists(long id) throws BaseException {
        Optional<NonPizza> nonPizza = nonPizzaRepository.findById(id);
        if(nonPizza.isPresent()){
            return true;
        }
        throw new ProductException("The product does not exist! Please add it first!");
    }
    public NonPizza getProductById(long id){
        Optional<NonPizza> product = nonPizzaRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        }
        else {
            return null;
        }
    }
}
