package utf8.citicup.dataService;

import org.springframework.data.jpa.repository.JpaRepository;
import utf8.citicup.domain.entity.Option;

public interface OptionRepository extends JpaRepository<Option,Long>{
}
