package dominos.demo.controller;

import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.AddressDao;
import dominos.demo.model.daos.UserDao;
import dominos.demo.model.users.Address;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
public class AddressController extends BaseController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AddressDao addressDao;


    @PostMapping(value = "/addAddress/{user_id}")
    public CommonResponseDTO addAddress(@PathVariable ("user_id") long user_id, @RequestBody Address address, HttpSession session) throws BaseException {
        if (SessionManager.isLoggedIn(session)) {
            User user = (User) session.getAttribute(SessionManager.LOGGED);
            if (user_id == user.getId()) {
                address.setUser_id(user.getId());
                addressDao.insertAddress(address);
                return new CommonResponseDTO(user.getFirst_name() + " " + user.getLast_name() + " added new address. ",
                        LocalDateTime.now());
            }
            else {
                return new CommonResponseDTO("User with id : " + user_id + " does not exist!", LocalDateTime.now());
            }
        }
        throw new InvalidLogInException("Please log in to add a new address");
    }
}
