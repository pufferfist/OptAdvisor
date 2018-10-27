package utf8.citicup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.historyEntity.OptionTsd;

import java.util.List;

@Component
public interface OptionRepository extends JpaRepository<Option,Long>{
    @Query("select o from Option o where o.expire=?1 and o.dynamic=1")
    List<Option> findOptionByExpireTime(String expireTime);
}
