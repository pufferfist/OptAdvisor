package utf8.citicup.dataServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import utf8.citicup.dao.OptionRepository;
import utf8.citicup.dao.PortfolioRepository;
import utf8.citicup.dataService.PortfolioDataService;
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
        return portfolioRepository.saveAndFlush(portfolio);
    }

    @Override
    @CacheEvict(value = "portfolio")
    public void delete(long id) {
        if(portfolioRepository.findById(id).isPresent()) {
            portfolioRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable(value = "portfolio")
    public Portfolio findById(long id) {
        Optional<Portfolio> portfolio=portfolioRepository.findById(id);
        if(!portfolio.isPresent())return null;
        Portfolio result=portfolio.get();
        return result;
    }

    @Override
    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    @Override
    public List<Portfolio> findByUsername(String username) {
        return portfolioRepository.findByUsername(username);
    }

    @Override
    @CacheEvict(value = "portfolio")
    public void updateTrackingStatus(long id, boolean trackingStatus) {
        portfolioRepository.updateReadStatus(id,trackingStatus);
    }

    @Override
    @CacheEvict(value = "portfolio")
    public void updateName(long id, String name) {
        portfolioRepository.updatePortfolioName(id, name);
    }
}
