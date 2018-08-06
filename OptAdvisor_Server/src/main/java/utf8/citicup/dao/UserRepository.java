package utf8.citicup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.User;

import java.util.List;
@Component
public interface UserRepository extends JpaRepository<User,String> {
    List<User> findByPassword(String password);

    List<User> findByUsername(String username);

    @Override
    @Modifying
    void delete(User user);


}
