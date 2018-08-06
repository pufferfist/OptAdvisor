package utf8.citicup.dataServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import utf8.citicup.dao.OptionRepository;
import utf8.citicup.dao.PortfolioRepository;
import utf8.citicup.dataService.PortfolioDataService;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioDataServiceImpl implements PortfolioDataService{
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private OptionRepository optionRepository;

    @Override
    @CachePut(value = "portfolio",key = "#portfolio.id")
    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    @Override
    @CacheEvict(value = "portfolio")
    public void delete(long id) {
        portfolioRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "portfolio")
    public Portfolio findById(long id) {
        Optional<Portfolio> portfolio=portfolioRepository.findById(id);
        if(!portfolio.isPresent())return null;
        Portfolio result=portfolio.get();
        autoSetOption(result);
        return result;
    }

    @Override
    public List<Portfolio> findByUsername(String username) {
        List<Portfolio> portfolioList= portfolioRepository.findByUsername(username);
        for(Portfolio each:portfolioList){
            autoSetOption(each);
        }
        return portfolioList;
    }

    /**
     * 为portfolio自动查找所属option并赋值
     */
    public void autoSetOption(Portfolio portfolio){
        List<Option> options=optionRepository.findByParentId(portfolio.getId());
        portfolio.setOptions(options.toArray(new Option[options.size()]));
    }
}
