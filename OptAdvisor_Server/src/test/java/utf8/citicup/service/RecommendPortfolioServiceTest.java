package utf8.citicup.service;/*
 * @author:Wu Gang
 * @create: 2018-08-28 09:29
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utf8.citicup.CiticupApplication;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(
        classes = CiticupApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class RecommendPortfolioServiceTest {
    @Autowired
    private RecommendService recommendService;
    private Logger logger = LoggerFactory.getLogger(RecommendService.class);

    @Test
    public void test01(){
        recommendService.recommendPortfolio(50000, 0.5, "2018-09", 'A', 2.53,
                2.60, 23, 24, 70, 30);
    }

    @Test
    public void test02(){
        recommendService.recommendPortfolio(500, 0.5, "2018-09", 'B', 2.53,
                2.60, 23, 24, 70, 30);
    }

    @Test
    public void test03(){
        recommendService.recommendPortfolio(50000, 0.5, "2018-09", 'C', 2.53,
                2.60, 23, 24, 70, 30);
    }

}
