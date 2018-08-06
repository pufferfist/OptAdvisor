package utf8.citicup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.Portfolio;

import java.util.List;

@Component
public interface PortfolioRepository extends JpaRepository<Portfolio,Long>{
    List<Portfolio> findByUsername(String username);
}
