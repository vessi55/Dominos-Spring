package dominos.demo.model.daos;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.pojos.products.Ingredient;
import dominos.demo.model.pojos.products.Pizza;
import dominos.demo.model.repositories.PizzaRepository;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidInputException;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import dominos.demo.model.pojos.enums.Size;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class PizzaDao {

    private static final String UPDATE_QUANTITY = "UPDATE pizzas SET quantity= ? WHERE id = ?";


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PizzaRepository pizzaRepository;


    public void addPizza(Pizza pizza){
        pizzaRepository.save(pizza);
    }

    public List<Pizza> getAllPizzas(){
        return pizzaRepository.findAll();
    }

    public List<Pizza> getPizzasByNameOrderedByPrice(String name){
        return pizzaRepository.findAllByNameOrderByPrice(name);
    }

    public List<Pizza> getAllPizzasByName(String name) {
        return pizzaRepository.findAllByName(name);
    }

    public List<Pizza> getPizzasPriceFilter(double price) { return pizzaRepository.findAllByPriceLessThan(price);}

    public Optional<Pizza> getById(Long aLong){
        return pizzaRepository.findById(aLong);
    }

    public Pizza getProductById(long id) throws ProductException {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if (pizza.isPresent()) {
            return pizza.get();
        }
        else {
            throw new ProductException("Pizza with id: " + id + " does not exist in database!");
        }
    }

    public Optional<Pizza> getByNameAndSize(String name, Size size){
        return pizzaRepository.findByNameAndSize(name, size);
    }

    public CommonResponseDTO deletePizzaById(long id) throws InvalidInputException {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if(pizza.isPresent()) {
            pizzaRepository.delete(pizza.get());
            return new CommonResponseDTO(pizza.get().getName()  + " was successfully deleted from database!", LocalDateTime.now());
        }
        throw new InvalidInputException("Pizza with id:" + id + " does not exist in database.");
    }

    public void changePizzaQuantity(long id, int quantity) throws BaseException {
        if(checkIfPizzaExists(id)){
            jdbcTemplate.update(UPDATE_QUANTITY, quantity, id);
        }
    }

    public boolean checkIfPizzaExists(long id) throws ProductException {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        if(pizza.isPresent()){
            return true;
        }
        throw new ProductException("The product does not exist! Please add it first!");
    }

    public double calculatePizzaPrice(HashMap<Pizza, HashSet<Ingredient>> pizzaExtras){
        double pizzaPrice = 0.0;
        for (Map.Entry<Pizza, HashSet<Ingredient>> e : pizzaExtras.entrySet() ) {
            pizzaPrice = e.getKey().getPrice();
            if (e.getValue().size() > 0) {
                double ingredientPrice = 0.0;
                for (Ingredient ingr : e.getValue()) {
                    ingredientPrice += ingr.getPrice();
                }
                pizzaPrice += ingredientPrice;
            }
        }
        return pizzaPrice;
    }

}
