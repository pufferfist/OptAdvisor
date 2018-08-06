package utf8.citicup.dataService;

import org.springframework.data.jpa.repository.JpaRepository;
import utf8.citicup.domain.entity.Message;

public interface MessageRepository extends JpaRepository<Message,Long>{
}
