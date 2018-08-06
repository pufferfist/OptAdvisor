package utf8.citicup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.User;

@Component
public interface UserRepository extends JpaRepository<User,String> {
    User findByUsername(String username);
}
