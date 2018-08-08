package utf8.citicup.dao.historyDao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.historyEntity.OptionTsd;
@Component
public interface OptionTsdRepository extends JpaRepository<OptionTsd,Long> {
    OptionTsd findByCodeNameAndLatestDate(String code,String latestDate);
}
