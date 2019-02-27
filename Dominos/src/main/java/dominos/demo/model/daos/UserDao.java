package dominos_project.demo.model.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class UserDao {

    @Autowired
    private UserRepository userRepository;

    public void register(User user){
        userRepository.save(user);
    }
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public User getUserByEmailAndPassword(String email, String password){
        return userRepository.findByEmailAndPassword(email, password);
    }




}
