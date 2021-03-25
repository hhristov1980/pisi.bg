package pisibg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pisibg.model.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmail(String email);

}
