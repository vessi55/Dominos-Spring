package dominos.demo.model.daos;

import dominos.demo.controller.UserController;
import dominos.demo.model.DTOs.UserEditDTO;
import dominos.demo.model.repositories.UserRepository;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Component
public class UserDao {

    private static final String EDIT_USER = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void registerUser(User user){
        userRepository.save(user);
    }
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public User getUserByEmailAndPassword(String email, String password){
        return userRepository.findByEmailAndPassword(email, password);
    }

    public String updateUser(@RequestBody UserEditDTO editUser, User user) throws BaseException, IOException {
        validName(editUser);
        validEmail(editUser);
        validPassword(editUser, user);
        jdbcTemplate.update(EDIT_USER, editUser.getNewFirstName(), editUser.getNewLastName(),
                editUser.getNewEmail(), editUser.getNewPassword(), user.getId());

        return "User updated!";
    }

    private boolean validName(UserEditDTO editUser) throws BaseException{
        if(!editUser.getNewFirstName().isEmpty() || editUser.getNewFirstName() != ""
                || editUser.getNewLastName().isEmpty() || editUser.getNewLastName() != "") {
            return true;
        }
        throw new InvalidLogInException("Empty First name OR Last name");
    }

    public boolean validEmail(UserEditDTO editUser) throws BaseException {
        if(!editUser.getNewEmail().isEmpty() || editUser.getNewEmail() != " ") {
            if (UserController.validEmailAddress(editUser.getNewEmail())) {
                return true;
            }
        }
        throw new InvalidLogInException("Wrong email!");
    }

    public boolean validPassword(UserEditDTO editUser, User user) throws BaseException {
        if (editUser.getCurrentPassword().equals(user.getPassword())) {
            if(editUser.getNewPassword().equals(editUser.getRepeatPassword())) {
                UserController.validPassword(editUser.getNewPassword());
                return true;
            }
        }
        throw new InvalidLogInException("Wrong password!");
    }

}
