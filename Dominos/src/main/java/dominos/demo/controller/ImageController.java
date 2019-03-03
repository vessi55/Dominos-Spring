package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.DTOs.ImageDTO;
import dominos.demo.model.daos.ProductDao;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.PizzaRepository;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;

@RestController
public class ImageController {

    public static final String IMAGE_DIR = "B:\\SPRING\\Dominos-Spring\\pictures\\";

    @Autowired
    private ProductDao productDao;
    @Autowired
    private PizzaRepository pizzaRepository;

    @PostMapping("pizzas/{name}/uploadImage")
    public CommonResponseDTO uploadImage(@RequestBody ImageDTO imageDTO, @PathVariable("name") String name, HttpSession session) throws Exception {
        if (SessionManager.validateLoginAdmin(session)) {
            Pizza pizza = productDao.getProductByName(name);
            if (pizza.getImage_url() == null) {
                String base64 = imageDTO.getImagePath();
                byte[] bytes = Base64.getDecoder().decode(base64);
                String imageName = pizza.getName() + System.currentTimeMillis() + ".png";
                File newImage = new File(IMAGE_DIR + imageName);
                FileOutputStream fos = new FileOutputStream(newImage);
                fos.write(bytes);
                pizza.setImage_url(imageName);
                pizzaRepository.save(pizza);
                return new CommonResponseDTO("Image uploaded successfully!", LocalDateTime.now());
            }
            throw new ProductException("Image already uploaded for pizza: " + pizza.getName());
        }
        else {
            throw new ProductException("You have no rights to upload an image!");
        }
    }

    @GetMapping(value="/pizzas/{name}/downloadImage", produces = "image/png")
    public byte[] downloadImage(@PathVariable("name") String name, HttpSession session) throws Exception {
        Pizza pizza = productDao.getProductByName(name);
        if(pizza.getImage_url() != null) {
            File newImage = new File(IMAGE_DIR + pizza.getImage_url());
            FileInputStream fis = new FileInputStream(newImage);
            return fis.readAllBytes();
        }
        throw new ProductException("No image found for pizza: " + pizza.getName());
    }

    @DeleteMapping("/pizzas/{name}/deleteImage")
    public CommonResponseDTO deleteImage(@PathVariable ("name") String name, HttpSession session) throws Exception {
        if (SessionManager.validateLoginAdmin(session)) {
            Pizza pizza = productDao.getProductByName(name);
            File newImage = new File(IMAGE_DIR + pizza.getImage_url());
            if (pizza.getImage_url() != null) {
                newImage.delete();
                pizza.setImage_url(null);
                return new CommonResponseDTO("Image deleted successfully!", LocalDateTime.now());
            }
            throw new ProductException("No image found for pizza: " + pizza.getName());
        }
        throw new ProductException("You have no rights to delete this image!");
    }
}
