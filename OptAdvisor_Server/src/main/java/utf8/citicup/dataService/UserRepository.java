package utf8.citicup.dataService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.User;

import javax.transaction.Transactional;
import java.util.List;
@Component
public interface UserRepository extends JpaRepository<User,String> {
/*    @Override
    @Modifying
    void delete(User user);*/

    @Override
    <S extends User> S save(S s);
}
