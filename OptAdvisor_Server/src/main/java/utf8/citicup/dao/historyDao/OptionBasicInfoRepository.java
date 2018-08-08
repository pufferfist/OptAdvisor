package utf8.citicup.dao.historyDao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.historyEntity.OptionBasicInfo;
@Component
public interface OptionBasicInfoRepository extends JpaRepository<OptionBasicInfo,String> {
}
