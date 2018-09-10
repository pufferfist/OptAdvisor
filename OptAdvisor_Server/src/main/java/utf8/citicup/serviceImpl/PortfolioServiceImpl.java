package utf8.citicup.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.PortfolioDataService;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.domain.entity.RecommendOption2;
import utf8.citicup.domain.entity.RecommendOption1;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.PortfolioService;
import utf8.citicup.service.util.StatusMsg;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utf8.citicup.domain.common.Type.*;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioDataService dataService;
//    private Logger logger = LoggerFactory.getLogger(PortfolioServiceImpl.class);

    @Autowired
    private RecommendServiceImpl recommendService;
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
            if(portfolio.getType() == RECOMMEND_PORTFOLIO){
                recommendService.setP1(portfolio.getP1());
                recommendService.setP2(portfolio.getP2());
                recommendService.setSigma1(portfolio.getSigma1());
                recommendService.setSigma2(portfolio.getSigma2());
                Option[] optionList = portfolio.getOptions().clone();
                RecommendOption1 recommendOption1 = null;
                try {
                    recommendOption1 = recommendService.mainTwoCustomPortfolio(optionList, 2,
                            portfolio.getK(), portfolio.getM0());
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseMsg(2000, "网络获取数据出错", null);
                }
                Portfolio showPortfolio = new Portfolio(portfolio.getName(), portfolio.getUsername(),recommendOption1,
                        RECOMMEND_PORTFOLIO, false);
                Portfolio[] rnt = new Portfolio[]{portfolio, showPortfolio};


                String[][] assertPrice2Profit = recommendOption1.getAssertPrice2Profit();
                String[][] profit2Probability = recommendOption1.getProfit2Probability();
                String[][] historyProfit2Probability = recommendOption1.getHistoryProfit2Probability();
                Map<String, Object> map = new HashMap<>();
                map.put("portfolios", rnt);
                map.put("assertPrice2Profit", assertPrice2Profit);
                map.put("profit2Probability", profit2Probability);
                map.put("historyProfit2Probability", historyProfit2Probability);
                return new ResponseMsg(0, "Get portfolio information success", map);
            }
            else if(portfolio.getType() == DIY){
                Option[] optionList = portfolio.getOptions().clone();
                RecommendOption1 recommendOption1 = null;
                try {
                    recommendOption1 = recommendService.mainOneCustomPortfolio(optionList, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseMsg(2000, "网络获取数据出错", null);
                }
                Portfolio showPortfolio = new Portfolio(portfolio.getName(), portfolio.getUsername(),
                        recommendOption1, DIY, false);
                Portfolio[] rnt = new Portfolio[]{portfolio, showPortfolio};
                String[][] assertPrice2Profit = recommendOption1.getAssertPrice2Profit();
                String[][] historyProfit2Probability = recommendOption1.getHistoryProfit2Probability();
                Map<String, Object> map = new HashMap<>();
                map.put("portfolios", rnt);
                map.put("assertPrice2Profit", assertPrice2Profit);
                map.put("historyProfit2Probability", historyProfit2Probability);
                return new ResponseMsg(0, "Get portfolio information success", map);
            }
            else if(portfolio.getType() == HEDGE){
                Option newOption;
                newOption = portfolio.getOptions()[0];

                try {
                    recommendService.setOptionAttributes(newOption);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                double iK = newOption.getK();
                double iNum = portfolio.getiNum();
                int findType;
                if(portfolio.isFlag()) findType=0;
                else findType=1;



                String[] T1 = newOption.getExpireTime().split("-");
                String T = T1[0]+'-'+T1[1];
                String[][] tempRtn = new String[][]{recommendService.getMonth(),null};
//                String[][] newRtn = recommendService.hedgingBackTest(findType,portfolio.getN(),portfolio.getiK(),portfolio.getpAsset(),T);

                RecommendOption2 recommendOption2 = new RecommendOption2(newOption, iK, iNum,tempRtn);


                Portfolio showPortfolio = new Portfolio(portfolio.getUsername(),recommendOption2,portfolio.getN(),portfolio.getpAsset(),portfolio.getsExp(),portfolio.isFlag(), iNum, HEDGE);

                Portfolio[] rtn = new Portfolio[]{portfolio,showPortfolio};

                String[] backTestData = portfolio.transformStringToStringlist();
                String[] backTestData1 = portfolio.transformStringToStringlist1();
                String[] difference = new String[backTestData.length];

                for(int i=0; i<backTestData.length;i++){
                    if("null".equals(backTestData[i])) break;
                    double value = Double.valueOf(backTestData[i]);
                    double value1 = Double.valueOf(backTestData1[i]);
                    double vaule3 = value1 - value;
                    difference[i] = Double.toString(vaule3);
                }

                String[][] graph = new String[][]{recommendOption2.getGraph()[0],backTestData,backTestData1,difference};

                Map<String, Object> map = new HashMap<>();
                map.put("portfolios", rtn);
                map.put("graph", graph);
                return new ResponseMsg(0, "Get portfolio information success", map);
            }
            return new ResponseMsg(0, "Get portfolio information success", portfolio);
        }
        else
            return StatusMsg.portfolioNotMatchUser;
    }

    @Scheduled(initialDelay = 100, fixedRate = 5 * 1000)
    public void task() {
    }
}
