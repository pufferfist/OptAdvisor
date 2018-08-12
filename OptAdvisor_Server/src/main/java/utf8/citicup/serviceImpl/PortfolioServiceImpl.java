package utf8.citicup.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.PortfolioDataService;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.PortfolioService;
import utf8.citicup.service.util.StatusMsg;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioDataService dataService;

    @Override
    public ResponseMsg addPortfolio(String username, Option[] list, Type type) {
        Portfolio portfolio = new Portfolio(username, list, type, false);
        dataService.save(portfolio);
//        Long id = getId(p);   //将对象加入数据库，并调用数据库方法， 获取新增组合后的id
        return StatusMsg.addPortfolioSuccess;
    }

    @Override
    public ResponseMsg riskTracking(String username, Long portfolioId) {
//        dataService.updateRiskTracking(username, portfolioId);
        return StatusMsg.updateRiskTrackingSuccess;
//        else
//        return StatusMsg.portfolioNotMatchUser;
//        TODO
    }

    @Override
    public ResponseMsg getPortfolio(String username) {
        return new ResponseMsg(0, "Get portfolio success", dataService.findByUsername(username));
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
