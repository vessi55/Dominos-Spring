package dominos.demo.controller;
import dominos.demo.model.DTOs.AddressResponseDTO;
import dominos.demo.model.daos.AddressDao;
import dominos.demo.model.users.Address;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidAddressException;
import dominos.demo.util.exceptions.InvalidLogInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class AddressController extends BaseController {


    @Autowired
    private AddressDao addressDao;

    @GetMapping(value = "/allAddresses")
    public List<Address> getAllAddresses(HttpSession session) throws InvalidLogInException
    {
        SessionManager.validateLoginAdmin(session);
        return addressDao.getAll();
    }

    @PostMapping(value = "/addAddress")
    public AddressResponseDTO addAddress(@RequestBody Address address, HttpSession session) throws BaseException {
        if (SessionManager.isLoggedIn(session)) {
            User user = (User) session.getAttribute(SessionManager.LOGGED);
            if (validAddress(address)) {
                address.setUser_id(user.getId());
                addressDao.insertAddress(address);
                return new AddressResponseDTO(address.getCity(), address.getStreet());
            }
            else {
                throw new InvalidAddressException("Invalid address!");
            }
        }
        throw new InvalidLogInException("Please log in to add a new address");
    }

    @GetMapping(value = "/users/myAddresses")
    public List<Address> getAddressesOfUser( Address address, HttpSession session) throws BaseException {
        if(SessionManager.isLoggedIn(session)) {
            User user = (User) session.getAttribute(SessionManager.LOGGED);
            return addressDao.getAddressesByUserId(user.getId());
        }
        throw new InvalidLogInException("Please log in to view all your addresses!");
    }

    public boolean validAddress(Address address) throws BaseException {
        if(address.getCity().isEmpty() || address.getCity() == null
                || address.getStreet().isEmpty() || address.getStreet() == null) {
            throw new InvalidAddressException("City/Street MUST NOT be empty!");

        }
        return true;
    }

}
