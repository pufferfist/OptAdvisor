package utf8.citicup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.Message;

import javax.transaction.Transactional;
import java.util.List;

@Component
public interface MessageRepository extends JpaRepository<Message,Long>{
    List<Message> findByIdAndUsername(Long id,String username);
    List<Message> findByUsername(String username);
    List<Message> findByUsernameAndReadStatus(String username,boolean readStatus);

    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update Message i set i.readStatus = ?3 where i.id =?1 and i.username=?2")
    public void updateReadStatus(Long id,String username,boolean readStatus);
}
