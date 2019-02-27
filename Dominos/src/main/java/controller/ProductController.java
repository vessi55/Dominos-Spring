package controller;


import dominos.demo.model.daos.ProductDao;
import dominos.demo.model.products.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.exceptions.BaseException;
import util.exceptions.ProductNotFoundException;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController extends BaseController {

    @Autowired
    ProductDao productDao;


    @GetMapping(value = "/products")
    public List<Product> getAll(HttpSession session) throws BaseException {
        validateLogin(session);
        return productDao.getAllProducts();
    }



//    @GetMapping(value = "/products/{id}")
//    public Product getById(@PathVariable("id") long id) throws BaseException{
//        Optional<Product> obj = productDao.findById(id);
//        if(obj.isPresent()) {
//            return obj.get();
//        }
//        else {
//            throw new ProductNotFoundException();
//        }
//    }


    //working
    @PostMapping(value = "/products/filter")
    public Optional<Product> getProductByName(@RequestParam("name") String name) throws BaseException{
        Optional<Product> product = productDao.getProductByName(name);
        if(product.isPresent()) {
            return product;
        }
        else {
            throw new ProductNotFoundException();
        }
    }
}
