package dominos.demo.model.repositories;

import dominos.demo.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findById(long id);
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
}
