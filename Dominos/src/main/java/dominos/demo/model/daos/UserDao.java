package dominos.demo.model.daos;

import dominos.demo.model.repositories.UserRepository;
import dominos.demo.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDao {
    @Autowired
    private UserRepository userRepository;

    public void registerUser(User user){
        userRepository.save(user);
    }
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public User getUserByEmailAndPassword(String email, String password){
        return userRepository.findByEmailAndPassword(email, password);
    }
}
