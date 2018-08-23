package utf8.citicup.controller;

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
import utf8.citicup.util.AuthenticationProcess;

import java.util.HashMap;
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
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static MockHttpSession httpSession;
    private static final String username = "mock";
    private static final String password = "mockPassword";
    private static final String newPassword = "mockNewPassword";
    private static final String oldName = "oldMock";
    private static final String newName = "newMock";

    private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

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

        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void test01SignUp1004() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("birthday", "2000/7/9");
        map.put("name", oldName);
        map.put("telephone", "17602529171");
        map.put("gender", "male");
        map.put("w1", 70);
        map.put("w2", 30);
        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1004));
    }

    @Test
    public void test01zIsUsernameExists() throws Exception {
        this.mockMvc.perform(get("/user/username?search=" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
        this.mockMvc.perform(get("/user/username?search=1" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    public void test020Auth() throws Exception {
        this.mockMvc.perform(get("/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1008));
    }

    @Test
    public void test020Login1001() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("username", username + "1");
        map.put("password", password);
        this.mockMvc.perform(post("/session")
                .content(objectToJsonString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1001));
    }

    @Test
    public void test020Login1002() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password + " ");
        this.mockMvc.perform(post("/session")
                .content(objectToJsonString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1002));
    }

    @Test
    public void test02Login() throws Exception {
        httpSession = AuthenticationProcess.login(mockMvc, username, password);
    }

    @Test
    public void test02zAuth() throws Exception {
        this.mockMvc.perform(get("/session").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void test03GetInfo() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/user").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0)).andReturn();
        Map<String, Object> map = jsonStringToMap(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(objectToMap(map.get("data")).get("name").toString(), oldName);
        httpSession = (MockHttpSession) mvcResult.getRequest().getSession();
    }

    @Test
    public void test04ModifyInfo() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("name", newName);
        map.put("w1", 70);
        map.put("w2", 30);

        this.mockMvc.perform(put("/user").session(httpSession)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void test05GetInfo() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/user").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn();
        Map<String, Object> map = jsonStringToMap(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(objectToMap(map.get("data")).get("name"), newName);
//        httpSession = (MockHttpSession) mvcResult.getRequest().getSession();
    }

    @Test
    public void test06ResetPassword() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("oldPassword", password);
        map.put("newPassword", newPassword);

//        this.mockMvc.perform(put("/user/password")
//                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(map)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(1001));

        this.mockMvc.perform(put("/user/password").session(httpSession)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        this.mockMvc.perform(put("/user/password").session(httpSession)
                .content(objectToJsonString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1002));
    }

    @Test
    public void test07LogoutAndTestAuth() throws Exception {
        this.mockMvc.perform(get("/session").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        AuthenticationProcess.logout(mockMvc, httpSession);

        this.mockMvc.perform(get("/session").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1008));

        this.mockMvc.perform(get("/user").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1008));
    }

    @Test
    public void test08LoginWithNewPassword() throws Exception {
        httpSession = AuthenticationProcess.login(mockMvc, username, newPassword);
    }

    @Test
    public void test09GetInfo() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/user").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0)).andReturn();

        Map<String, Object> resultMap = jsonStringToMap(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(objectToMap(resultMap.get("data")).get("name").toString(), newName);
    }

    @Test
    public void test98Logout() throws Exception {
        AuthenticationProcess.logout(mockMvc, httpSession);
        httpSession = AuthenticationProcess.login(mockMvc, "suun", "suunPassword");
    }

    @Test
    public void test99DeleteUser() throws Exception {
        this.mockMvc.perform(
                delete("/admin/user/" + username).session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }
}
