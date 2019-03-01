package dominos.demo.model.repositories;

import dominos.demo.model.users.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    //Address findAllByUser(User user);
}
