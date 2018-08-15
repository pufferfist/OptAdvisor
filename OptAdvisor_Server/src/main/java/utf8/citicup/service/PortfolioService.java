package utf8.citicup.service;


import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;

public interface PortfolioService {
    public Long addPortfolio(String username, Option[] list,Type type);
    public boolean riskTracking(Long portfolioId);
    public Portfolio[] getPortfolio(String username);
    public Portfolio getPortfolioInfo(Long portfolioId);
}