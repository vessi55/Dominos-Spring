package controller;


import dominos.demo.model.daos.UserDao;
import dominos.demo.model.DTOs.*;
import dominos.demo.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import util.exceptions.BaseException;
import util.exceptions.InvalidLogInException;
import util.exceptions.InvalidLogOutException;
import util.exceptions.InvalidRegistrationException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController extends BaseController {

    public static final String LOGGED = "loggedIn";

    @Autowired
    private UserDao userDao;


//    @PostMapping(value = "/register")
//    public User register(@RequestBody RegisterUserDTO regUser, HttpServletResponse response, HttpSession session) throws Exception {
//        String first_name = regUser.getFirst_name();
//        String last_name = regUser.getLast_name();
//        String email = regUser.getEmail();
//        String pass1 = regUser.getPassword();
//        String pass2 = regUser.getPassword2();
//
//        User user = new User(first_name, last_name,email,pass1,pass2);
//        if (registerValidation(regUser)) {
//            userDao.register(user);
//            //SessionManager.logUser(session, user);
//        } else {
//            response.setStatus(400);
//            throw new InvalidRegistrationException("Problem with registration!");
//        }
//        return user;
//    }
    @PostMapping(value = "/register")
    public User register(@RequestBody UserRegisterDTO regUser, HttpServletResponse response, HttpSession session) throws BaseException {
        User user = null;
        if (registerValidation(regUser)) {
            user = new User();
            user.setFirst_name(regUser.getFirst_name());
            user.setLast_name(regUser.getLast_name());
            user.setEmail(regUser.getEmail());
            user.setPassword(regUser.getPassword());
            userDao.registerUser(user);
            //SessionManager.logUser(session,user);
        }
        return user;
    }


    @PostMapping(value = "/login")
    public LoginResponseUserDTO loginUser(@RequestBody UserLogInDTO login, HttpSession session) throws InvalidLogInException {
        if(!(SessionManager.isLoggedIn(session))&& validateLogIn(login)){
            String email = login.getEmail();
            String password = login.getPassword();
            User user = userDao.getUserByEmailAndPassword(email, password);
            if(user == null){
                throw new InvalidLogInException("User with this email does not exist!");
            }
            SessionManager.logUser(session, user);
            return new LoginResponseUserDTO(user.getFirst_name(), user.getLast_name(),
                    user.getEmail(), new Date());
        }
        throw new InvalidLogInException("You are already logged!");
    }

    @GetMapping(value = "/logout")
    public void logout(HttpSession session,HttpServletResponse response) throws Exception {
        if (SessionManager.isLoggedIn(session)) {
            session.invalidate();
            response.getWriter().append("You logged out successfully!");
        }
        throw new InvalidLogOutException("You are already logged out!");
    }


    private boolean validateLogIn(UserLogInDTO user) throws InvalidLogInException {
        String email = user.getEmail();
        String password = user.getPassword();
        if (email == null || email.isEmpty()||password == null || password.isEmpty()) {
            throw new InvalidLogInException("Invalid input!Please, try again!");
        }
        return true;
    }
    public boolean registerValidation(UserRegisterDTO user) throws InvalidRegistrationException {
        if(!checkForEmptyAndNullValue(user) || !validEmailAddress(user.getEmail()) ||!validPassword(user.getPassword())
            || !checkIfPasswordsMatching(user) || !checkIfEmailExists(user)){
            throw new InvalidRegistrationException("Invalid input");
        }
        return true;
    }


    public boolean checkIfEmailExists(UserRegisterDTO user) throws InvalidRegistrationException{
        if (userDao.getUserByEmail(user.getEmail()) != null) {
            throw new InvalidRegistrationException("Email already exists!");
        }
        return true;
    }

    public boolean checkForEmptyAndNullValue(UserRegisterDTO user) throws InvalidRegistrationException {
        String pass1 = user.getPassword().trim();
        String pass2 = user.getPassword2().trim();
        String first_name = user.getFirst_name();
        String last_name = user.getLast_name();
        String email =  user.getEmail();
        if(first_name.isEmpty() || first_name == null ||
                last_name.isEmpty() || last_name == null ||
                email.isEmpty() || email == null) {
            throw new InvalidRegistrationException("Your first name, last name and email MUST NOT be empty!");
        }
        else if(pass1.isEmpty() || pass1 == null ||
                pass2.isEmpty() ||pass2 == null){
            throw  new InvalidRegistrationException("Password MUST not be empty!");
        }
        return true;
    }
    public boolean checkIfPasswordsMatching(UserRegisterDTO user)throws InvalidRegistrationException {
        if (!(user.getPassword().equals(user.getPassword2()))) {
            throw new InvalidRegistrationException("Passwords MUST be matching");
        }
        return true;
    }
    public boolean validPassword(String password) throws InvalidRegistrationException {
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
        String pass = ("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Pattern pattern = Pattern.compile(pass);
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()) {
            throw new InvalidRegistrationException("Password must contain at least: one lowercase letter, one uppercase " +
                    "letter and one digit.At least one special character like : .*[@#$%^&+=." +
                    "Password length must be minimum 8 symbols.");
        }
        return true;
    }
    public boolean validEmailAddress(String email) throws InvalidRegistrationException {
        //String regex = "^(.+)@(.+)$";
        /*
 [A-Z0-9._%+-]+ - the first part of mail address may contain all characters, numbers, points, underscores, percent, plus and minus.
@ - the @ character is mandatory
[A-Z0-9.-]+ - the second part of mail address may contain all characters, numbers, points, underscores.
\. - the point is mandatory
[A-Z]{2,4} - the domain name may contain all characters. The number of characters is limited between 2 and 4.
        */

        String regex = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,4}\n";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) {
            throw new InvalidRegistrationException("Invalid email address!");
        }
        return true;
    }
}
