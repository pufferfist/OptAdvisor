package utf8.citicup.service;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utf8.citicup.CiticupApplication;
import utf8.citicup.domain.entity.ResponseMsg;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(
        classes = CiticupApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecommendHedgingServiceTest {

    @Autowired
    private RecommendService recommendService;
    private Logger logger = LoggerFactory.getLogger(RecommendService.class);



    @Test
    public void test01() {

//        int N0 = 100000;
//        double a = 0.5;
//        double sExp = 2.1;
//        String T = "2018-10";
        int N0 = 50000;
        double a = 0.25;
        double sExp = 2.2;
        String T = "2018-10";




//        recommendService.hedging(N0, a, sExp, T);



        ResponseMsg rspMsg = recommendService.hedging(N0, a, sExp, T);


//        Option OptionI = objectToAnyType(objectToMap(rspMsg.getData()).get("rtn"), Option.class);
//        double iK = objectToAnyType(objectToMap(rspMsg.getData()).get("rtn"), Double.class);
//        logger.info("最后的iK是" + Double.toString(iK));
//        String[][] rtn = objectToAnyType(objectToMap(rspMsg.getData()).get("rtn"), String[][].class);

//        logger.info(Arrays.deepToString(rtn));





//        Assert.assertEquals(objectToMap(map.get("data")).get("name"), newName);
    }
}
