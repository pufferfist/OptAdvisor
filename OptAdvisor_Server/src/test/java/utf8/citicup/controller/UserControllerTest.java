package utf8.citicup.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import utf8.citicup.CiticupApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@SpringBootTest(
        classes = CiticupApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static MockHttpSession JSessionId;
    private static final String username = "mock";
    private static final String password = "mockPassword";
    private static final String newPassword = "mockNewPassword";
    private static final String oldName = "oldMock";
    private static final String newName = "newMock";

    private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    private String mapToJsonString(Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private Map<String, Object> jsonStringToMap(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private Map objectToMap(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(object, Map.class);
    }

    @Test
    public void test01SignUp() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("birthday", "2000/7/9");
        map.put("name", oldName);
        map.put("telephone", "17602529171");
        map.put("gender", "male");
        map.put("w1", 70);
        map.put("w2", 30);

        MvcResult mvcResult = this.mockMvc.perform(post("/signUp")
                .contentType(MediaType.APPLICATION_JSON).content(mapToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn();
        logger.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void test02Login() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        MvcResult mvcResult = this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(mapToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0)).andReturn();
        JSessionId = (MockHttpSession) mvcResult.getRequest().getSession();
    }

    @Test
    public void test03GetInfo() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/user/getInfo").session(JSessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0)).andReturn();
        Map<String, Object> map = jsonStringToMap(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(objectToMap(map.get("data")).get("name").toString(), oldName);
        JSessionId = (MockHttpSession) mvcResult.getRequest().getSession();
    }

    @Test
    public void test04ModifyInfo() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("name", newName);
        map.put("w1", 70);
        map.put("w2", 30);

        this.mockMvc.perform(post("/user/modifyInfo").session(JSessionId)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void test05GetInfo() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/user/getInfo").session(JSessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn();
        Map<String, Object> map = jsonStringToMap(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(objectToMap(map.get("data")).get("name"), newName);
        JSessionId = (MockHttpSession) mvcResult.getRequest().getSession();
    }

//    @Test
//    public void test06ResetPassword() throws Exception {
//        Map<String, Object> map = new HashMap<>();
//        map.put("oldPassword", password);
//        map.put("newPassword", newPassword);
//        this.mockMvc.perform(post("user/resetPassword").session(JSessionId)
//                .contentType(MediaType.APPLICATION_JSON).content(mapToJsonString(map)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(0));
//    }

    @Test
    public void test06ResetPassword() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("oldPassword", password);
        map.put("newPassword", newPassword);

        this.mockMvc.perform(post("/user/resetPassword").session(JSessionId)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void test07LogoutAndTestAuth() throws Exception {
        this.mockMvc.perform(post("/user/logout").session(JSessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        this.mockMvc.perform(post("user/getInfo").session(JSessionId))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void test08LoginWithNewPassword() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", newPassword);
        MvcResult mvcResult = this.mockMvc.perform(post("/login").session(JSessionId)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0)).andReturn();
        JSessionId = (MockHttpSession) mvcResult.getRequest().getSession();
//        Map<String, Object> resultMap = jsonStringToMap(mvcResult.getResponse().getContentAsString());
//        Assert.assertEquals(objectToMap(resultMap.get("data")).get("name").toString(), newName);
    }

    @Test
    public void test09GetInfo() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/user/getInfo").session(JSessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0)).andReturn();

        Map<String, Object> resultMap = jsonStringToMap(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(objectToMap(resultMap.get("data")).get("name").toString(), newName);
    }

    @Test
    public void test99DeleteUser() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        this.mockMvc.perform(
                post("/user/private/deleteUser").session(JSessionId)
                        .contentType(MediaType.APPLICATION_JSON).content(mapToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }
}
