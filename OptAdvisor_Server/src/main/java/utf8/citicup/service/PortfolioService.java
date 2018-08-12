package utf8.citicup.service;


import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.ResponseMsg;

public interface PortfolioService {
    ResponseMsg addPortfolio(String username, Option[] list, Type type);
    ResponseMsg riskTracking(String username, Long portfolioId);
    ResponseMsg getPortfolio(String username);
    ResponseMsg getPortfolioInfo(String username, Long portfolioId);
}
