package dominos.demo.model.daos;

import dominos.demo.model.repositories.AddressRepository;
import dominos.demo.model.users.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AddressDao {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertAddress(Address address){
        addressRepository.save(address);
    }
}
