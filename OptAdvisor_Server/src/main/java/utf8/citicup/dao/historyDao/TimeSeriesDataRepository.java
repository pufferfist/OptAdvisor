package utf8.citicup.dao.historyDao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.historyEntity.TimeSeriesData;
@Component
public interface TimeSeriesDataRepository extends JpaRepository<TimeSeriesData,Long> {
    TimeSeriesData findByLastTradeDate(String lastTradeDate);
}
