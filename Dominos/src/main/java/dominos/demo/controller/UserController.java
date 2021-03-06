package dominos.demo.controller;

import dominos.demo.model.DTOs.*;
import dominos.demo.model.daos.UserDao;
import dominos.demo.model.repositories.UserRepository;
import dominos.demo.model.pojos.users.User;
import dominos.demo.util.BCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidLogInException;
import dominos.demo.util.exceptions.InvalidRegistrationException;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController extends BaseController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/register")
    public UserResponseDTO register(@RequestBody UserRegisterDTO regUser, HttpSession session) throws BaseException {
        User user = null;
        if (validateRegistration(regUser)) {
            user = new User();
            user.setFirst_name(regUser.getFirst_name());
            user.setLast_name(regUser.getLast_name());
            user.setEmail(regUser.getEmail());
            user.setPassword(BCryptUtil.hashPassword(regUser.getPassword()));
            userRepository.save(user);
            SessionManager.logUser(session,user);
        }
        return new UserResponseDTO(user.getId(),user.getFirst_name(), user.getLast_name(),
                user.getEmail(), LocalDateTime.now());
    }

    @PostMapping(value = "/login")
    public UserResponseDTO loginUser(@RequestBody UserLogInDTO login, HttpSession session) throws InvalidLogInException {
            validateLogIn(login);
            String email = login.getEmail();
            String password = login.getPassword();
            User user = userDao.getUserByEmail(email);
            if(user == null){
                throw new InvalidLogInException("User with this email does not exist!");
            }
            else if(!BCryptUtil.checkPass(password,user.getPassword())){
                throw new InvalidLogInException("Wrong password!");
            }
            SessionManager.logUser(session, user);
            return new UserResponseDTO(user.getId(),user.getFirst_name(), user.getLast_name(),
                    user.getEmail(), LocalDateTime.now());
    }

    @PostMapping(value = "/logout")
    public CommonResponseDTO logout(HttpSession session) throws Exception {
        if(SessionManager.isLoggedIn(session)) {
            session.invalidate();
            return new CommonResponseDTO("You logged out successfully!", LocalDateTime.now());
        }
        throw new InvalidLogInException("You are already logged out");
    }

    @PutMapping(value = "/editProfile")
    public UserResponseDTO editProfile(@RequestBody UserEditDTO editUser, HttpSession session) throws Exception {
        if(SessionManager.isLoggedIn(session)) {
            User user = (User) session.getAttribute(SessionManager.LOGGED);
            userDao.updateUser(editUser, user);
            return new UserResponseDTO(user.getId(),editUser.getNewFirstName(), editUser.getNewLastName(), user.getEmail(),
                    LocalDateTime.now());
        }
        throw new InvalidLogInException("Please log in to edit your profile!");
    }

    @DeleteMapping(value = "/deleteProfile")
    public CommonResponseDTO deleteProfile(HttpSession session) throws Exception {
        if(SessionManager.isLoggedIn(session)) {
            User user = (User) session.getAttribute(SessionManager.LOGGED);
            userRepository.delete(user);
            session.invalidate();
            return new CommonResponseDTO("Profile successfully deleted! ", LocalDateTime.now());
        }
        throw new InvalidLogInException("Please log in to delete your profile!");
    }

    @GetMapping(value = "/users/{name}")
    public String getByName(@PathVariable("name") String name){
        return "Welcome, " + name;
    }

    @PutMapping(value = "/users/{id}/setAdmin")
    public CommonResponseDTO setAdmin(@PathVariable ("id") long id, HttpSession session) {
        User user = userRepository.findById(id);
        user.setAdmin(true);
        userRepository.save(user);
        return new CommonResponseDTO("ADMIN SUCCESSFULL!", LocalDateTime.now());
    }

    private boolean validateLogIn(UserLogInDTO user) throws InvalidLogInException {
        String email = user.getEmail();
        String password = user.getPassword();
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidLogInException("Invalid input! Please, try again!");
        }
        return true;
    }

    private boolean validateRegistration(UserRegisterDTO user) throws InvalidRegistrationException {
        if(user.getFirst_name().isEmpty() || user.getFirst_name() == null
                || user.getLast_name().isEmpty() || user.getLast_name() == null
                || !validEmailAddress(user.getEmail()) ||!validPassword(user.getPassword())
                || !checkIfPasswordsMatching(user) || !checkIfEmailExists(user)){
            throw new InvalidRegistrationException("Invalid input");
        }
        return true;
    }

    private boolean checkIfEmailExists(UserRegisterDTO user) throws InvalidRegistrationException{
        if (userDao.getUserByEmail(user.getEmail()) != null) {
            throw new InvalidRegistrationException("User with this email already exists!");
        }
        return true;
    }

    private boolean checkIfPasswordsMatching(UserRegisterDTO user)throws InvalidRegistrationException {
        if (!(user.getPassword().equals(user.getPassword2()))) {
            throw new InvalidRegistrationException("Passwords MUST be matching");
        }
        return true;
    }

    public static boolean validPassword(String password) throws InvalidRegistrationException {
        /*
^                 # start-of-string
(?=.*[0-9])       # a digit must occur at least once
(?=.*[a-z])       # a lower case letter must occur at least once
(?=.*[A-Z])       # an upper case letter must occur at least once
(?=.*[@#$%^&+=])  # a special character must occur at least once
(?=\S+$)          # no whitespace allowed in the entire string
.{8,}             # anything, at least eight places though
$                 # end-of-string
        */
        String pass = ("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        Pattern pattern = Pattern.compile(pass);
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()) {
            throw new InvalidRegistrationException("Password must contain at least: one lowercase letter, one uppercase " +
                    "letter and one digit.At least one special character like : .*[@#$%^&+=." +
                    "Password length must be minimum 8 symbols.");
        }
        return true;
    }

    public static boolean validEmailAddress(String email) throws InvalidRegistrationException {
        //String regex = "^(.+)@(.+)$";
        // \w - letters, digits and _
        //\b means "word boundary"
        String regex = "\\b[\\w.-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) {
            throw new InvalidRegistrationException("Invalid email address!");
        }
        return true;

    }
}
