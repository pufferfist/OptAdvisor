package utf8.citicup.dataServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import utf8.citicup.dao.OptionRepository;
import utf8.citicup.dataService.OptionDataService;
import utf8.citicup.domain.entity.Option;

import java.util.List;

@Service
public class OptionDataServiceImpl implements OptionDataService{
    @Autowired
    private OptionRepository optionRepository;

    @Override
    @CachePut(value = "option",key = "#option.id")
    public Option save(Option option) {
        return optionRepository.save(option);
    }

    @Override
    @CacheEvict(value = "option")
    public void delete(long id) {
        if(optionRepository.findById(id).isPresent()) {
            optionRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable(value = "option")
    public Option findById(long id) {
        return optionRepository.findById(id).orElse(null);
    }
}
