package dominos.demo.model.repositories;

import dominos.demo.model.pojos.users.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    @Override
    List<Address> findAll();
}
