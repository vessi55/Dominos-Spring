package dominos.demo.model.daos;

import dominos.demo.controller.SessionManager;
import dominos.demo.controller.UserController;
import dominos.demo.model.DTOs.UserEditDTO;
import dominos.demo.model.repositories.UserRepository;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

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

    public void updateUser(@RequestBody UserEditDTO editUser, User user) throws BaseException {
        validName(editUser);
        validEmail(editUser);
        validPassword(editUser, user);
        jdbcTemplate.update(EDIT_USER, editUser.getNewFirstName(), editUser.getNewLastName(),
                editUser.getNewEmail(), editUser.getNewPassword(), user.getId());

    }

    public void deleteUser(@PathVariable long idParam, HttpSession session) throws Exception {
        if(SessionManager.isLoggedIn(session)) {
            User user = (User) session.getAttribute(SessionManager.LOGGED);
            if (user.getId() == idParam) {
                userRepository.delete(user);
                session.invalidate();
            }
        }
        else {
            throw new InvalidLogInException("Please log in to delete your profile!");
        }
    }

    private boolean validName(UserEditDTO editUser) throws BaseException{
        if(!editUser.getNewFirstName().isEmpty() || editUser.getNewFirstName() != ""
                || !editUser.getNewLastName().isEmpty() || editUser.getNewLastName() != "") {
            return true;
        }
        throw new InvalidLogInException("Empty First name OR Last name");
    }

    public boolean validEmail(UserEditDTO editUser) throws BaseException {
        if(!editUser.getNewEmail().isEmpty() || editUser.getNewEmail() != "") {
            if (UserController.validEmailAddress(editUser.getNewEmail())) {
                return true;
            }
        }
        throw new InvalidLogInException("Invalid email!");
    }

    public boolean validPassword(UserEditDTO editUser, User user) throws BaseException {
        if (editUser.getCurrentPassword().equals(user.getPassword())) {
            if(editUser.getNewPassword().equals(editUser.getRepeatPassword())) {
                UserController.validPassword(editUser.getNewPassword());
                return true;
            }
        }
        throw new InvalidLogInException("Invalid password!");
    }

}
