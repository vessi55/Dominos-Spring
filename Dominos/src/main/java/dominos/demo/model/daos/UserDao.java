package dominos.demo.model.daos;

import dominos.demo.controller.UserController;
import dominos.demo.model.DTOs.UserEditDTO;
import dominos.demo.model.repositories.UserRepository;
import dominos.demo.model.pojos.users.User;
import dominos.demo.util.BCryptUtil;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class UserDao {

    private static final String EDIT_USER = "UPDATE users SET first_name = ?, last_name = ?, password = ? WHERE id = ?";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getUserByEmailAndPassword(String email, String password){
        return userRepository.findByEmailAndPassword(email, password);
    }

    public void updateUser(@RequestBody UserEditDTO editUser, User user) throws BaseException {
        validName(editUser);
        validPassword(editUser, user);
        jdbcTemplate.update(EDIT_USER, editUser.getNewFirstName(), editUser.getNewLastName(),
                BCryptUtil.hashPassword(editUser.getNewPassword()), user.getId());

    }

    private boolean validName(UserEditDTO editUser) throws BaseException{
        if(!editUser.getNewFirstName().isEmpty() || editUser.getNewFirstName() != ""
                || !editUser.getNewLastName().isEmpty() || editUser.getNewLastName() != "") {
            return true;
        }
        throw new InvalidLogInException("Empty First name OR Last name");
    }

    public boolean validPassword(UserEditDTO editUser, User user) throws BaseException {
        if(BCryptUtil.checkPass(editUser.getCurrentPassword(),user.getPassword())){
            if(editUser.getNewPassword().equals(editUser.getRepeatPassword())) {
                UserController.validPassword(editUser.getNewPassword());
                return true;
            }
        }
        throw new InvalidLogInException("Invalid password!");
    }

}
