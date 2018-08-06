package utf8.citicup.dataService;

import utf8.citicup.domain.entity.Portfolio;

import java.util.List;

public interface PortfolioDataService {
    public Portfolio save(Portfolio portfolio);
    public void delete(long id);
    public Portfolio findById(long id);
    public List<Portfolio> findByUsername(String username);
}
