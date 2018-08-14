package utf8.citicup.controller;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.PortfolioService;
import utf8.citicup.service.util.JsonParse;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    private Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    @RequestMapping(value = "portfolio", method = RequestMethod.POST)
    public ResponseMsg addPortfolio(@RequestBody Map<String, Object> params) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        Option[] options = JsonParse.objectToAnyType(params.get("options"), Option[].class);
        Type type = JsonParse.objectToAnyType(params.get("type"), Type.class);
        return portfolioService.addPortfolio(username, options, type);
    }

    @RequestMapping(value = "portfolio/track", method = RequestMethod.PUT)
    public ResponseMsg riskTracking(@RequestBody Map<String, Object> params) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        Long id = Long.parseLong(params.get("id").toString());
        logger.info(String.valueOf(id));
        return portfolioService.riskTracking(username, id);
    }

    @RequestMapping(value = "portfolio", method = RequestMethod.GET)
    public ResponseMsg getPortfolio() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.getPortfolio(username);
    }

    @RequestMapping(value = "portfolio/{portfolioId}", method = RequestMethod.GET)
    public ResponseMsg getPortfolioInfo(@Valid @PathVariable Long portfolioId) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.getPortfolioInfo(username, portfolioId);
    }
}
