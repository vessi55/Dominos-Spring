package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.PizzaDao;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.PizzaRepository;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;

@RestController
public class PizzaImageController extends BaseController{

    public static final String IMAGE_DIR = "B:\\Dominos\\";

    @Autowired
    private PizzaDao pizzaDao;

    @Autowired
    private PizzaRepository pizzaRepository;


    @PostMapping("pizzas/{id}/uploadImage")
    public CommonResponseDTO uploadImage(@RequestPart(value = "image") MultipartFile file, @PathVariable("id") long id, HttpSession session) throws Exception {
        SessionManager.validateLoginAdmin(session);
        Pizza pizza = pizzaDao.getProductById(id);
        if(pizza.getImage_url() != null) {
            File image = new File(IMAGE_DIR + pizza.getImage_url());
            image.delete();
        }
        String imageName = pizza.getName() + System.currentTimeMillis() + ".png";
        File newImage = new File(IMAGE_DIR + imageName);
        file.transferTo(newImage);
        pizza.setImage_url(imageName);
        pizzaRepository.save(pizza);
        return new CommonResponseDTO("Image uploaded successfully for pizza with id: " + id, LocalDateTime.now());
    }


    @GetMapping(value="/pizzas/{id}/downloadImage", produces = "image/png")
    public byte[] downloadImage(@PathVariable("id") long id, HttpSession session) throws Exception {
        Pizza pizza = pizzaDao.getProductById(id);
        if(pizza.getImage_url() != null) {
            File newImage = new File(IMAGE_DIR + pizza.getImage_url());
            FileInputStream fis = new FileInputStream(newImage);
            return fis.readAllBytes();
        }
        throw new ProductException("No image found for pizza with id: " + id);
    }


    @DeleteMapping("/pizzas/{id}/deleteImage")
    public CommonResponseDTO deleteImage(@PathVariable ("id") long id, HttpSession session) throws Exception {
        SessionManager.validateLoginAdmin(session);
        Pizza pizza = pizzaDao.getProductById(id);
        if (pizza.getImage_url() != null) {
            File newImage = new File(IMAGE_DIR + pizza.getImage_url());
            newImage.delete();
            pizza.setImage_url(null);
            pizzaRepository.save(pizza);
            return new CommonResponseDTO("Image deleted successfully for pizza with id: " + id, LocalDateTime.now());
        }
        else {
            throw new ProductException("No image found for pizza with id: " + id);
        }
    }
}
