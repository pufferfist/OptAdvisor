package utf8.citicup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.User;

import javax.transaction.Transactional;

@Component
public interface UserRepository extends JpaRepository<User,String> {
    User findByUsername(String username);

    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update User i set i.password = ?2 where i.username =?1")
    public void updatePassword(String username,String password);
}
