package utf8.citicup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.Portfolio;

import javax.transaction.Transactional;
import java.util.List;

@Component
public interface PortfolioRepository extends JpaRepository<Portfolio,Long>{
    List<Portfolio> findByUsername(String username);

    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update Portfolio i set i.trackingStatus = ?2 where i.id =?1")
    public void updateReadStatus(Long id,boolean trackingStatus);

    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("update Portfolio i set i.name = ?2 where  i.id = ?1")
    void updatePortfolioName(Long id, String name);
}
