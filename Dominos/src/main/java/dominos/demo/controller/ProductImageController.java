package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.DTOs.ImageDTO;
import dominos.demo.model.daos.NonPizzaDao;
import dominos.demo.model.products.NonPizza;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.NonPizzaRepository;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductImageController extends BaseController{

    public static final String IMAGE_DIR = "B:\\Dominos\\";

    @Autowired
    private NonPizzaDao nonPizzaDao;

    @Autowired
    private NonPizzaRepository nonPizzaRepository;


    @PostMapping("products/{name}/uploadImage")
    public CommonResponseDTO uploadImage(@RequestBody ImageDTO imageDTO, @PathVariable("name") String name, HttpSession session) throws Exception {
        SessionManager.validateLoginAdmin(session);
        Optional<NonPizza> nonPizza = nonPizzaDao.getByName(name);
        if (nonPizza.get().getImage_url() == null) {
            String base64 = imageDTO.getImagePath();
            byte[] bytes = Base64.getDecoder().decode(base64);
            String imageName = nonPizza.get().getName() + System.currentTimeMillis() + ".png";
            File newImage = new File(IMAGE_DIR + imageName);
            FileOutputStream fos = new FileOutputStream(newImage);
            fos.write(bytes);
            nonPizza.get().setImage_url(imageName);
            nonPizzaRepository.save(nonPizza.get());
            return new CommonResponseDTO("Image uploaded successfully!", LocalDateTime.now());
        }
        else {
            return new CommonResponseDTO("Image already exist for product with name: " + name, LocalDateTime.now());
        }
    }

    @GetMapping(value="/products/{id}/downloadImage", produces = "image/png")
    public byte[] downloadImage(@PathVariable("id") long id, HttpSession session) throws Exception {
        Optional<NonPizza> nonPizza = nonPizzaDao.getById(id);
        if(nonPizza.get().getImage_url() != null) {
            File newImage = new File(IMAGE_DIR + nonPizza.get().getImage_url());
            FileInputStream fis = new FileInputStream(newImage);
            return fis.readAllBytes();
        }
        throw new ProductException("No image found for pizza: " + nonPizza.get().getName());
    }


    @DeleteMapping("/products/{id}/deleteImage")
    public CommonResponseDTO deleteImage(@PathVariable ("id") long id, HttpSession session) throws BaseException {
        SessionManager.validateLoginAdmin(session);
        Optional<NonPizza> nonPizza = nonPizzaDao.getById(id);
        File newImage = new File(IMAGE_DIR + nonPizza.get().getImage_url());
        if (nonPizza.get().getImage_url() != null) {
            newImage.delete();
            nonPizza.get().setImage_url(null);
            nonPizzaRepository.save(nonPizza.get());
            return new CommonResponseDTO("Image deleted successfully!", LocalDateTime.now());
        }
        else {
            return new CommonResponseDTO("No image found for product with id: " + id, LocalDateTime.now());
        }
    }
}
