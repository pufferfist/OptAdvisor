package utf8.citicup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.Option;

import java.util.List;

@Component
public interface OptionRepository extends JpaRepository<Option,Long>{
}
