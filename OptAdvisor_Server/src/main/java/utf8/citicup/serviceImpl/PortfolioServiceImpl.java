package utf8.citicup.serviceImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.PortfolioDataService;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.PortfolioService;
import utf8.citicup.service.util.StatusMsg;

import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioDataService dataService;

    private Logger logger = LoggerFactory.getLogger(PortfolioServiceImpl.class);

    @Override
    public ResponseMsg addPortfolio(String username, Option[] options, Type type) {
        Portfolio portfolio = new Portfolio(username, options, type, false);
        logger.info(String.valueOf(portfolio.getOptions().length));
        dataService.save(portfolio);
        return StatusMsg.addPortfolioSuccess;
    }

    @Override
    public ResponseMsg riskTracking(String username, Long portfolioId) {
        Portfolio portfolio = dataService.findById(portfolioId);
        if (null == portfolio) {
            return StatusMsg.portfolioNotExists;
        } else if (portfolio.getUsername().equals(username)) {
            logger.info(String.valueOf(portfolio.isTrackingStatus()));
            dataService.updateTrackingStatus(portfolioId, !portfolio.isTrackingStatus());
            return StatusMsg.updateRiskTrackingSuccess;
        } else {
            return StatusMsg.portfolioNotMatchUser;
        }
    }

    @Override
    public ResponseMsg getPortfolio(String username) {
        List<Portfolio> portfolioList = dataService.findByUsername(username);
        return new ResponseMsg(0, "Get portfolio success", portfolioList);
    }

    @Override
    public ResponseMsg deletePortfolio(String username, Long portfolioId) {
        Portfolio portfolio = dataService.findById(portfolioId);
        if (null == portfolio) {
            return StatusMsg.portfolioNotExists;
        } else if (portfolio.getUsername().equals(username)) {
            dataService.delete(portfolioId);
            return StatusMsg.deletePortfolioSuccess;
        } else {
            return StatusMsg.portfolioNotMatchUser;
        }
    }

    @Override
    public ResponseMsg getPortfolioInfo(String username, Long portfolioId) {
        Portfolio portfolio = dataService.findById(portfolioId);
        if (null == portfolio)
            return StatusMsg.portfolioNotExists;
        else if (portfolio.getUsername().equals(username))
            return new ResponseMsg(0, "Get portfolio information success", portfolio);
        else
            return StatusMsg.portfolioNotMatchUser;
    }
}
