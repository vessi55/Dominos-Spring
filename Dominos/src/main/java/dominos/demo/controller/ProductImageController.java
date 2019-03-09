package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.DTOs.ImageDTO;
import dominos.demo.model.daos.NonPizzaDao;
import dominos.demo.model.products.NonPizza;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.NonPizzaRepository;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidLogInException;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@RestController
public class ProductImageController extends BaseController{

    public static final String IMAGE_DIR = "B:\\Dominos\\";

    @Autowired
    private NonPizzaDao nonPizzaDao;

    @Autowired
    private NonPizzaRepository nonPizzaRepository;


    @PostMapping("products/{id}/uploadImage")
    public CommonResponseDTO uploadImage(@RequestPart(value = "image") MultipartFile file, @PathVariable("id") long id, HttpSession session) throws Exception {
        SessionManager.validateLoginAdmin(session);
        NonPizza nonPizza = nonPizzaDao.getProductById(id);
        if(nonPizza.getImage_url() != null) {
            File image = new File(IMAGE_DIR + nonPizza.getImage_url());
            image.delete();
        }
        String imageName = nonPizza.getName() + System.currentTimeMillis() + ".png";
        File newImage = new File(IMAGE_DIR + imageName);
        file.transferTo(newImage);
        nonPizza.setImage_url(imageName);
        nonPizzaRepository.save(nonPizza);
        return new CommonResponseDTO("Image uploaded successfully for product with id: !" + id, LocalDateTime.now());
    }

    @GetMapping(value="/products/{id}/downloadImage", produces = "image/png")
    public byte[] downloadImage(@PathVariable("id") long id, HttpSession session) throws Exception {
        NonPizza nonPizza = nonPizzaDao.getProductById(id);
        if(nonPizza.getImage_url() != null) {
            File newImage = new File(IMAGE_DIR + nonPizza.getImage_url());
            FileInputStream fis = new FileInputStream(newImage);
            return fis.readAllBytes();
        }
        throw new ProductException("No image found for product with id: " + id);
    }


    @DeleteMapping("/products/{id}/deleteImage")
    public CommonResponseDTO deleteImage(@PathVariable ("id") long id, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        NonPizza nonPizza = nonPizzaDao.getProductById(id);
        if (nonPizza.getImage_url() != null) {
            File newImage = new File(IMAGE_DIR + nonPizza.getImage_url());
            newImage.delete();
            nonPizza.setImage_url(null);
            nonPizzaRepository.save(nonPizza);
            return new CommonResponseDTO("Image deleted successfully for product with id: " + id, LocalDateTime.now());
        }
        else {
            return new CommonResponseDTO("No image found for product with id: " + id, LocalDateTime.now());
        }
    }
}
