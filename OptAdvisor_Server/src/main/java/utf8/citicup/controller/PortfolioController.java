package utf8.citicup.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.PortfolioService;

import java.util.Map;

@Controller
@RequestMapping(value = "portfolio", method = RequestMethod.POST)
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping("addPortfolio")
    public ResponseMsg addPortfolio(@RequestParam Map<String, Object> params) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return null;
//        return portfolioService.addPortfolio(username, params.get("option"), params.get("type"));
//        TODO
    }

    @PostMapping("riskTracking")
    public ResponseMsg riskTracking(Long portfolioId) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.riskTracking(username, portfolioId);
    }

    @PostMapping("getPortfolio")
    public ResponseMsg getPortfolio() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.getPortfolio(username);
    }

    @PostMapping("getPortfolioInfo")
    public ResponseMsg getPortfolioInfo(Long portfolioId) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.getPortfolioInfo(username, portfolioId);
    }
}
