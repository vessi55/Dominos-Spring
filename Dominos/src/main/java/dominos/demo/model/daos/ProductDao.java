package dominos.demo.model.daos;

import dominos.demo.model.products.Product;
import dominos.demo.model.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductDao {
    @Autowired
    ProductRepository productRepository;

//    public Optional<Product> getProductById(Long id){
//        return productRepository.findAllById(id);
//    }
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    public Optional<Product> getProductByName(String name){
        return productRepository.findByName(name);
    }
    //maybe will use for favorites or for orders
    public void saveProduct(Product p){
        productRepository.save(p);
    }

}
