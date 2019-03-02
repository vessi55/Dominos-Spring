package dominos.demo.controller;

import dominos.demo.model.DTOs.AddressResponseDTO;
import dominos.demo.model.DTOs.CommonResponseDTO;
import dominos.demo.model.daos.AddressDao;
import dominos.demo.model.daos.UserDao;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.AddressRepository;
import dominos.demo.model.users.Address;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AddressController extends BaseController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressDao addressDao;

    @GetMapping(value = "/allAddresses")
    public List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }

    @PostMapping(value = "/addAddress/{user_id}")
    public CommonResponseDTO addAddress(@PathVariable ("user_id") long user_id, @RequestBody Address address, HttpSession session) throws BaseException {
        if (SessionManager.isLoggedIn(session)) {
            User user = (User) session.getAttribute(SessionManager.LOGGED);
            if (user_id == user.getId()) {
                address.setUser_id(user.getId());
                addressDao.insertAddress(address);
                return new CommonResponseDTO(user.getFirst_name() + " " + user.getLast_name() + " added new address!",
                        LocalDateTime.now());
            }
            else {
                return new CommonResponseDTO("You have no rights to add a new address.", LocalDateTime.now());
            }
        }
        throw new InvalidLogInException("Please log in to add a new address");
    }

    @GetMapping(value = "/users/{user_id}/addresses")
    public List<AddressResponseDTO> getAddressesOfUser(@PathVariable ("user_id") long user_id, Address address, HttpSession session)
        throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            User user = (User) session.getAttribute(SessionManager.LOGGED);
            if(user.getId() == address.getUser_id()) {
                if (address.getUser_id() == user_id) {
                    return addressDao.getAllUserAddresses(user_id);
                }
            }
        }
        throw new InvalidLogInException("Please log in to view all your addresses!");
    }

}
