package utf8.citicup.service;/*
 * @author:Wu Gang
<<<<<<< HEAD
 * @create: 2018-08-23 09:54
 */

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utf8.citicup.CiticupApplication;
import utf8.citicup.serviceImpl.RecommendServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(
        classes = CiticupApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecommendPortfolioServiceTest {

    @Autowired
    private RecommendService recommendService;

    @Test
    public void test01() {
        recommendService.recommendPortfolio(50000, 0.5, "2018-09", 'G', 2.2,
                3.0, 20, 21, 70, 30);
    }

    @Test
    public void test02() {
        recommendService.recommendPortfolio(50000, 0.5, "2018-09", 'B', 2.2,
                3.0, 20, 21, 70, 30);
    }

    @Test
    public void test03() {
        recommendService.recommendPortfolio(50000, 0.5, "2018-10", 'C', 2.2,
                3.0, 20, 21, 70, 30);
    }

    @Test
    public void test04() {
        recommendService.recommendPortfolio(50000, 0.5, "2018-10", 'D', 2.2,
                3.0, 20, 21, 70, 30);
    }
}
