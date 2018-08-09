package utf8.citicup.serviceImpl;


import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.service.PortfolioService;

public class PortfolioServiceImpl implements PortfolioService {

    @Override
    public Long addPortfllio(String username, Option[] list, Type type) {
        Portfolio p = new Portfolio(username, list, type, false);
//        Long id = getId(p);   //将对象加入数据库，并调用数据库方法， 获取新增组合后的id
        return null;
    }

    @Override
    public boolean riskTracking(Long portfolioId) {
//        Portfolio p = findByPortfolioId(portfolioId); //根据portfolioId 找到该组合对象
//        boolean status = !p.isTrackingStatus();
//        p.setTrackingStatus(status);

        return true;//无论如何都返回true，因为只要点击了基本没出错
    }

    @Override
    public Portfolio[] getPortfolio(String username) {
        Portfolio[] p;
//        p = getPortfolioByusername(username);

//        return p;
        return new Portfolio[0];
    }

    @Override
    public Portfolio getPortfolioInfo(Long portfolioId) {
//        Portfolio p = findByPortfolioId(portfolioId); //根据portfolioId 找到该组合对象
//        return  p;
        return null;
    }
}
