package utf8.citicup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.Message;

import java.util.List;

@Component
public interface MessageRepository extends JpaRepository<Message,Long>{
    List<Message> findByUserName(String username);
    List<Message> findByUserNameAndReadStatus(String username,boolean readStatus);
}
