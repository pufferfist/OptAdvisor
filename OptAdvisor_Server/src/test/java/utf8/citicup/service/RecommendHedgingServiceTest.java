package utf8.citicup.service;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utf8.citicup.CiticupApplication;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.serviceImpl.RecommendServiceImpl;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static utf8.citicup.service.util.JsonParse.objectToAnyType;
import static utf8.citicup.service.util.JsonParse.objectToMap;

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

        int N0 = 100000;
        double a = 0.5;
        double sExp = 2.1;
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
