package dominos.demo.model.daos;

import dominos.demo.model.DTOs.AddressResponseDTO;
import dominos.demo.model.products.Pizza;
import dominos.demo.model.repositories.AddressRepository;
import dominos.demo.model.users.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AddressDao {

    public static final String ALL_ADDRESSES_OF_USER = "SELECT city, street FROM addresses WHERE user_id = ?";

    private static final String ALL_ADDRESSES_IN_CITY = "SELECT city, street FROM addresses WHERE city = ?";

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertAddress(Address address){
        addressRepository.save(address);
    }

    public List<AddressResponseDTO> getAllUserAddresses(long user_id) {
        List<AddressResponseDTO> addresses =  jdbcTemplate.query(ALL_ADDRESSES_OF_USER, new Object[]{user_id}, new BeanPropertyRowMapper<>(AddressResponseDTO.class));
        return addresses;
    }

}
