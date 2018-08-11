package utf8.citicup.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.RecommendService;
import utf8.citicup.service.UserService;

import java.util.Map;

@RestController
@RequestMapping(value = "recommend", method = RequestMethod.POST)
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private UserService userService;

    @PostMapping("recommendPortfolio")
    public ResponseMsg recommendPortfolio(@RequestBody Map<String, Object> params) {
        User user = userService.getUser(SecurityUtils.getSubject().getPrincipal().toString());
        return recommendService.recommendPortfolio((Double) params.get("M0"), (Double) params.get("k"),
                params.get("T").toString(), (char) params.get("combination"),
                (Double) params.get("p1"), (Double) params.get("p2"), (Double) params.get("sigma1"),
                (Double) params.get("sigma2"), user.getW1(), user.getW2());
    }

    @PostMapping("hedging")
    public ResponseMsg hedging(@RequestBody Map<String, Object> params) {
        return recommendService.hedging((Integer) params.get("N0"), (Double) params.get("a"),
                (Double) params.get("s_exp"), params.get("T").toString());
    }

    @PostMapping("customPortfolio")
    @ResponseBody
    public ResponseMsg customPortfolio(@RequestBody Option[] options) {
        return recommendService.customPortfolio(options);
    }
}
