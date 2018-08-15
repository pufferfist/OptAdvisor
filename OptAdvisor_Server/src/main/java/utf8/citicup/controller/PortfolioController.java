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

    @PostMapping("portfolio")
    public ResponseMsg addPortfolio(@RequestBody Map<String, Object> params) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        Option[] options = JsonParse.objectToAnyType(params.get("options"), Option[].class);
        Type type = JsonParse.objectToAnyType(params.get("type"), Type.class);
        return portfolioService.addPortfolio(username, options, type);
    }

    @PatchMapping("portfolio/{id}/track")
    public ResponseMsg riskTracking(@PathVariable Long id) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.riskTracking(username, id);
    }

    @GetMapping("portfolio")
    public ResponseMsg getPortfolio() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.getPortfolio(username);
    }

    @DeleteMapping("portfolio/{portfolioId}")
    public ResponseMsg deletePortfolio(@PathVariable Long portfolioId) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.deletePortfolio(username, portfolioId);
    }

    @GetMapping("portfolio/{portfolioId}")
    public ResponseMsg getPortfolioInfo(@Valid @PathVariable Long portfolioId) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return portfolioService.getPortfolioInfo(username, portfolioId);
    }
}
