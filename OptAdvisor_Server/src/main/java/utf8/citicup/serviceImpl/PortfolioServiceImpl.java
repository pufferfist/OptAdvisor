package utf8.citicup.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.PortfolioDataService;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.domain.entity.RecommendOption2;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.PortfolioService;
import utf8.citicup.service.util.StatusMsg;

import java.io.IOException;
import java.util.List;

import static utf8.citicup.domain.common.Type.*;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioDataService dataService;

//    private Logger logger = LoggerFactory.getLogger(PortfolioServiceImpl.class);

    @Override
    public ResponseMsg addPortfolio(Portfolio portfolio) {
//        logger.info(String.valueOf(portfolio.getOptions().length));
        dataService.save(portfolio);
        return StatusMsg.addPortfolioSuccess;
    }

    @Override
    public ResponseMsg riskTracking(String username, Long portfolioId) {
        Portfolio portfolio = dataService.findById(portfolioId);
        if (null == portfolio) {
            return StatusMsg.portfolioNotExists;
        } else if (portfolio.getUsername().equals(username)) {
//            logger.info(String.valueOf(portfolio.isTrackingStatus()));
            dataService.updateTrackingStatus(portfolioId, !portfolio.isTrackingStatus());
            return StatusMsg.updateRiskTrackingSuccess;
        } else {
            return StatusMsg.portfolioNotMatchUser;
        }
    }

    @Override
    public ResponseMsg updateName(String username, Long portfolioId, String portfolioName) {
        Portfolio portfolio = dataService.findById(portfolioId);
        if (null == portfolio) {
            return StatusMsg.portfolioNotExists;
        } else if (portfolio.getUsername().equals(username)) {
            dataService.updateName(portfolioId, portfolioName);
            return StatusMsg.updatePortfolioNameSuccess;
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
        else if (portfolio.getUsername().equals(username)) {
            RecommendServiceImpl recommendService = new RecommendServiceImpl();
            if(portfolio.getType() == RECOMMEND_PORTFOLIO){
                recommendService.setP1(portfolio.getP1());
                recommendService.setP2(portfolio.getP2());
                recommendService.setSigma1(portfolio.getSigma1());
                recommendService.setSigma2(portfolio.getSigma2());
                Option[] optionList = portfolio.getOptions().clone();
                Portfolio showPortfolio = new Portfolio(portfolio.getName(), portfolio.getUsername(),
                        recommendService.mainTwoCustomPortfolio(optionList), RECOMMEND_PORTFOLIO, false);
                Portfolio[] rnt = new Portfolio[]{portfolio, showPortfolio};
                return new ResponseMsg(0, "Get portfolio information success", rnt);
            }
            else if(portfolio.getType() == DIY){
                Option[] optionList = portfolio.getOptions().clone();
                Portfolio showPortfolio = new Portfolio(portfolio.getName(), portfolio.getUsername(),
                        recommendService.mainOneCustomPortfolio(optionList), DIY, false);
                Portfolio[] rnt = new Portfolio[]{portfolio, showPortfolio};
                return new ResponseMsg(0, "Get portfolio information success", rnt);
            }
            else if(portfolio.getType() == HEDGE){
                Option newOption = new Option();
                newOption = portfolio.getOptions()[0];

                try {
                    recommendService.setOptionAttributes(newOption);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                double iK = newOption.getK();
                int findType;
                if(portfolio.isFlag()) findType=0;
                else findType=1;


                String[] T1 = newOption.getExpireTime().split("-");
                String T = T1[0]+'-'+T1[1];
                String[][] newRtn = recommendService.hedgingBackTest(findType,portfolio.getN(),portfolio.getiK(),portfolio.getpAsset(),T);

                RecommendOption2 recommendOption2 = new RecommendOption2(newOption, iK, newRtn);

                Portfolio showPortfolio = new Portfolio(portfolio.getUsername(),recommendOption2,portfolio.getN(),portfolio.getpAsset(),portfolio.getsExp(),portfolio.isFlag(),HEDGE);

                Portfolio[] rtn = new Portfolio[]{portfolio,showPortfolio};
                return new ResponseMsg(0, "Get portfolio information success", rtn);
            }
            return new ResponseMsg(0, "Get portfolio information success", portfolio);
        }
        else
            return StatusMsg.portfolioNotMatchUser;
    }

    @Scheduled(initialDelay = 1000, fixedRate = 5 * 1000)
    public void task() {
//        logger.info("task running");
    }
}
