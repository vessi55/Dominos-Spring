package dominos.demo.model.daos;

import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PizzaDao {

    @Autowired
    PizzaRepository pizzaRepository;


    public void addPizza(Pizza pizza){
        pizzaRepository.save(pizza);
    }
    public List<Pizza> getAllPizas(){
       return pizzaRepository.findAll();
    }
    public Optional<Pizza> findPizzaByName(String name){
        return pizzaRepository.findByName(name);
    }

}
