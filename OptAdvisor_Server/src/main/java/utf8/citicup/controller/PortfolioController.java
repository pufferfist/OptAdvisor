package utf8.citicup.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.PortfolioService;
import utf8.citicup.service.util.JsonParse;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

//    private Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    @PostMapping("portfolio")
    public ResponseMsg addPortfolio(@Valid @RequestBody Map<String, Object> params) {
        Option[] options = JsonParse.objectToAnyType(params.get("options"), Option[].class);
        Type type = JsonParse.objectToAnyType(params.get("type"), Type.class);
        String[][] listOfStringList = JsonParse.objectToAnyType(params.get("graph"), String[][].class);
        params.remove("options");
        params.remove("type");
        params.remove("graph");

        Portfolio portfolio = JsonParse.objectToAnyType(params, Portfolio.class);
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        portfolio.setUsername(username);

        portfolio.setOptions(options);
        portfolio.setType(type);
        portfolio.setBuildTime(new java.sql.Timestamp(new java.util.Date().getTime()));
        if(listOfStringList != null) {
            if (listOfStringList.length > 1)
                portfolio.transformStringlistToString(listOfStringList[1]);
            if (listOfStringList.length > 2)
                portfolio.transformStringlistToString1(listOfStringList[2]);
        }
        return portfolioService.addPortfolio(portfolio);
    }

    @PatchMapping("portfolio/{id}/track")
    public ResponseMsg riskTracking(@Valid @PathVariable Long id) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.riskTracking(username, id);
    }

    @PutMapping("portfolio/{id}/name")
    public ResponseMsg updatePortfolioName(@Valid @PathVariable Long id, @Valid @RequestBody Map<String, String> params) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.updateName(username, id, params.get("name"));
    }

    @GetMapping("portfolio")
    public ResponseMsg getPortfolio() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.getPortfolio(username);
    }

    @DeleteMapping("portfolio/{portfolioId}")
    public ResponseMsg deletePortfolio(@Valid @PathVariable Long portfolioId) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.deletePortfolio(username, portfolioId);
    }

    @GetMapping("portfolio/{portfolioId}")
    public ResponseMsg getPortfolioInfo(@Valid @PathVariable Long portfolioId) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.getPortfolioInfo(username, portfolioId);
    }
}
