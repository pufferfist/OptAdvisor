package utf8.citicup.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import utf8.citicup.CiticupApplication;
import utf8.citicup.controller.util.AuthenticationProcess;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utf8.citicup.service.util.JsonParse.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@SpringBootTest(
        classes = CiticupApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PortfolioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Logger logger = LoggerFactory.getLogger(PortfolioControllerTest.class);

    private static MockHttpSession httpSession;

    private static final String oldName = "portfolioOldName";
    private static final String newName = "portfolioNewName";
    private static Long portfolioId;

    @Before
    public void login() throws Exception {
        String username = "suun";
        String password = "suunPassword";

        httpSession = AuthenticationProcess.login(mockMvc, username, password);
    }

    @Test
    public void test01addPortfolio() throws Exception {
        Option option = new Option("", "CON_OP_10001281", "optionName", 1, 2,
                "expireTime", 3.0, 4.0, 5.0, 6.0, 7.0, 8.0,
                3.0, 4.0, 5.0, 6.0, 7.0, 8.0);
        Option[] options = {option, option};
        Portfolio portfolio = new Portfolio();
        {
            portfolio.setName(oldName);
            portfolio.setOptions(options);
            portfolio.setType(Type.RECOMMEND_PORTFOLIO);
            portfolio.setTrackingStatus(false);
            portfolio.setM0(1.0);
            portfolio.setK(2.0);
            portfolio.setSigma1(3.0);
            portfolio.setSigma2(4.0);
            portfolio.setP1(5.0);
            portfolio.setP2(6.0);
            portfolio.setCost(7.0);
            portfolio.setBond(8.0);
            portfolio.setZ_delta(9.0);
            portfolio.setZ_gamma(10.0);
            portfolio.setZ_vega(11.0);
            portfolio.setZ_theta(12.0);
            portfolio.setZ_rho(13.0);
            portfolio.setEM(14.0);
            portfolio.setBeta(15.0);
            portfolio.setN(16);
            portfolio.setiK(17.0);
            portfolio.setpAsset(18.0);
            portfolio.setsExp(19.0);
            portfolio.setFlag(false);
        }
        this.mockMvc.perform(post("/portfolio").session(httpSession)
                .content(objectToJsonString(portfolio)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    public List<Portfolio> getAllPortfolio() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/portfolio").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0)).andReturn();
        Map<String, Object> response = jsonStringToMap(mvcResult.getResponse().getContentAsString());
        Object[] objects = objectToAnyType(response.get("data"), Object[].class);
        List<Portfolio> generatePortfolioList = new ArrayList<>();
        for (Object object : objects) {
            Map portfolioMap = objectToMap(object);

            Option[] options = objectToAnyType(portfolioMap.get("options"), Option[].class);
            portfolioMap.remove("options");
            Type type = objectToAnyType(portfolioMap.get("type"), Type.class);
            portfolioMap.remove("type");

            Portfolio portfolio = objectToAnyType(portfolioMap, Portfolio.class);
            portfolio.setOptions(options);
            portfolio.setType(type);

            generatePortfolioList.add(portfolio);
        }
        return generatePortfolioList;
    }

    @Test
    public void test02GetPortfolio() throws Exception {
        List<Portfolio> portfolioList = getAllPortfolio();
        Assert.assertEquals(portfolioList.size(), 1);
        portfolioId = portfolioList.get(0).getId();
        Assert.assertEquals(portfolioList.get(0).getName(), oldName);
        Assert.assertFalse(portfolioList.get(0).isTrackingStatus());
//        Assert.assertEquals(portfolioList.get(0).getName(), oldName);
    }

    @Test
    public void test03SetTrackingStatus() throws Exception {

        this.mockMvc.perform(patch("/portfolio/" + portfolioId + "/track").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        this.mockMvc.perform(patch("/portfolio/10000/track").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(3002));

        Portfolio portfolio = getAllPortfolio().get(0);
        Assert.assertTrue(portfolio.isTrackingStatus());
    }

    @Test
    public void test04ResetPortfolioName() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("name", newName);
        this.mockMvc.perform(put("/portfolio/" + portfolioId + "/name").session(httpSession)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
        Assert.assertEquals(getAllPortfolio().get(0).getName(), newName);
    }

    @Test
    public void test99DeletePortfolios() throws Exception {
        List<Portfolio> portfolioList = getAllPortfolio();
        for (Portfolio portfolio : portfolioList) {
            Long portfolioId = portfolio.getId();

            this.mockMvc.perform(delete("/portfolio/" + portfolioId).session(httpSession))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0));
        }
        logger.info(String.valueOf(getAllPortfolio().size()));
        Assert.assertEquals(getAllPortfolio().size(), 0);
    }
}
