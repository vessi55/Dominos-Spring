package dominos.demo.model.daos;

import dominos.demo.model.repositories.AddressRepository;
import dominos.demo.model.users.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AddressDao {

    public static final String ALL_ADDRESSES_OF_USER = "SELECT id,city, street, user_id FROM addresses WHERE user_id = ?";

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertAddress(Address address){
        addressRepository.save(address);
    }

    public List<Address> getAll() { return addressRepository.findAll();}

    public List<Address> getAddressesByUserId(long user_id){
        List<Address> addresses =  jdbcTemplate.query(ALL_ADDRESSES_OF_USER, new Object[]{user_id}, new BeanPropertyRowMapper<>(Address.class));
        return addresses;
    }
}
