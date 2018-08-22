package utf8.citicup.service;


import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.domain.entity.ResponseMsg;

public interface PortfolioService {
    ResponseMsg addPortfolio(Portfolio portfolio);
    ResponseMsg riskTracking(String username, Long portfolioId);
    ResponseMsg getPortfolio(String username);
    ResponseMsg deletePortfolio(String username, Long portfolioId);
    ResponseMsg getPortfolioInfo(String username, Long portfolioId);
    ResponseMsg updateName(String username, Long portfolioId, String portfolioName);
}