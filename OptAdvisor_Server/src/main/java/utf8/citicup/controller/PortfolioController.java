package utf8.citicup.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
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
        return new ResponseMsg(0, "Add portfolio success",
                portfolioService.addPortfolio(username, (Option[]) params.get("option"), (Type) params.get("type")));
    }

    @PostMapping("riskTracking")
    public ResponseMsg riskTracking(Long portfolioId) {
        return new ResponseMsg(0, "Get risk tracking success", portfolioService.riskTracking(portfolioId));
    }

    @PostMapping("getPortfolio")
    public ResponseMsg getPortfolio() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return new ResponseMsg(0, "Get portfolio success", portfolioService.getPortfolio(username));
    }

    @PostMapping("getPortfolioInfo")
    public ResponseMsg getPortfolioInfo(Long portfolioId) {
        return new ResponseMsg(0, "Get portfolio information success", portfolioService.getPortfolioInfo(portfolioId));
    }
}
