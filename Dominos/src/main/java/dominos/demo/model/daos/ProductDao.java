package dominos.demo.model.daos;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.PizzaRepository;
import dominos.demo.util.exceptions.InvalidInputException;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ProductDao {

    private static final String UPDATE_QUANTITY = "UPDATE pizzas SET quantity= ? WHERE id = ?";
    private static final String EDIT_PIZZA = "UPDATE pizzas SET name = ?, description = ?, size = ?, weight = ?  , price = ?  , image_url = ? WHERE id = ?";



    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addProduct(Pizza pizza){
        pizzaRepository.save(pizza);
    }
    public  Optional<Pizza> getById(long id){
        return pizzaRepository.findById(id);
    }
    public List<Pizza> getAllPizzas(){
        return pizzaRepository.findAll();
    }
    public Pizza getProductByName(String name) {
        Optional<Pizza> product = pizzaRepository.findByName(name);
        if (product.isPresent()) {
            return product.get();
        }
        else {
            return null;
        }
    }
    public Optional<Pizza> getByName(String name){
        return pizzaRepository.findByName(name);
    }

    public boolean checkIfProductExist(long id) throws ProductException {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if(pizza.isPresent()){
            return true;
        }
        throw new ProductException("The product  does not exist! Please add it first!");
    }
    public void changePizzaQuantity(long id, int quantity) throws ProductException {
        if(checkIfProductExist(id)){
            jdbcTemplate.update(UPDATE_QUANTITY, quantity,id);
        }
    }
    public CommonResponseDTO deleteProductById(long id) throws InvalidInputException {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if(pizza.isPresent()) {
           pizzaRepository.delete(pizza.get());
           return new CommonResponseDTO(pizza.get().getName()  + " was successfully deleted from database!", LocalDateTime.now());
        }
        throw new InvalidInputException("The pizza does not exist in  database.");

    }
    public List<Pizza> getAllPizzaNamesOrderedByPrice(String name){
        return pizzaRepository.findAllByNameOrderByPrice(name);
    }


    //TODO : add in DB quantity for pizzas non-pizzas and ingredients


}
