package utf8.citicup.dao.historyDao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.historyEntity.OptionTsd;

import java.util.List;

@Component
public interface OptionTsdRepository extends JpaRepository<OptionTsd,Long> {
    OptionTsd findByCodeNameAndLatestDate(String code,String latestDate);

    @Query("select t3 from OptionTsd t3 where t3.latestDate=?1 and t3.codeName in(\n" +
            " select t2.codeName from OptionBasicInfo t2,TimeSeriesData t1\n" +
            " where t2.type=?3 and t2.endDate=?2 and t2.price>t1.closePrice and t1.lastTradeDate=?1)")
    List<OptionTsd> complexFindHigh(String startDate,String endDate,boolean type);

    @Query("select t3 from OptionTsd t3 where t3.latestDate=?1 and t3.codeName in(\n" +
            " select t2.codeName from OptionBasicInfo t2,TimeSeriesData t1\n" +
            " where t2.type=?3 and t2.endDate=?2 and t2.price<t1.closePrice and t1.lastTradeDate=?1)")
    List<OptionTsd> complexFindLow(String startDate,String endDate,boolean type);
}
