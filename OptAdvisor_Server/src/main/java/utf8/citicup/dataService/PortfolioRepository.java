package utf8.citicup.dataService;

import org.springframework.data.jpa.repository.JpaRepository;
import utf8.citicup.domain.entity.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long>{
}
