package utf8.citicup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.RecommendService;

import java.util.Map;

@RestController
@RequestMapping(value = "recommend", method = RequestMethod.POST)
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @PostMapping("recommendPortfolio")
    public ResponseMsg recommendPortfolio(@RequestBody Map<String, Object> params) {
        return recommendService.recommendPortfolio((Double) params.get("M0"), (Double) params.get("k"),
                (Double) params.get("a"), (Integer) params.get("T"), (char) params.get("combination"),
                (Double) params.get("p1"), (Double) params.get("p2"), (Double) params.get("sigma1"),
                (Double) params.get("sigma2"));
    }

    @PostMapping("hedging")
    public ResponseMsg hedging(@RequestBody Map<String, Object> params) {
        return recommendService.heging((Integer) params.get("N0"), (Double) params.get("a"),
                (Double) params.get("s_exp"), (int) params.get("T"));
    }

    @PostMapping("customPortfolio")
    public ResponseMsg customPortfolio(@RequestBody Map<String, Object> params) {

        return null;
    }
}
