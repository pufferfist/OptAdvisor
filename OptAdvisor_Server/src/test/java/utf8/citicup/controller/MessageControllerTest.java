package utf8.citicup.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import utf8.citicup.domain.entity.Message;
import utf8.citicup.controller.util.AuthenticationProcess;

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
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Logger logger = LoggerFactory.getLogger(MessageControllerTest.class);

    private static final String firstTitle = "first title";
    private static final String username = "suun";
    private static final String password = "suunPassword";
    private static final String firstContent = "first content";
    private static final String secondTitle = "second title";
    private static final String secondContent = "second content";
    private ObjectMapper mapper = new ObjectMapper();

    private static MockHttpSession httpSession;

    @Before
    public void login() throws Exception {
        httpSession = AuthenticationProcess.login(mockMvc, username, password);
    }

    @Test
    public void test01PostMessage() throws Exception {
        Message message = new Message(username, firstContent, firstTitle);

        this.mockMvc.perform(post("/admin/message").session(httpSession)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(message)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    private Map<String, List<Message>> getMessage() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/message").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn();
        Map<String, Object> map = jsonStringToMap(mvcResult.getResponse().getContentAsString());
        Map<String, List<Message>> result = new HashMap<>();
        result.put("read", this.mapper.convertValue(objectToMap(map.get("data")).get("read"), new TypeReference<List<Message>>() {
        }));
        result.put("unread", this.mapper.convertValue(objectToMap(map.get("data")).get("unread"), new TypeReference<List<Message>>() {
        }));
        return result;
    }

    @Test
    public void test02GetMessage() throws Exception {
        Message message = this.getMessage().get("unread").get(0);
        Assert.assertEquals(message.getMessage(), firstContent);
        Assert.assertFalse(message.getReadStatus());
        Assert.assertEquals(message.getTitle(), firstTitle);
        Assert.assertEquals(message.getUsername(), username);
    }

    @Test
    public void test03PostSecondMessage() throws Exception {
        Message message = new Message(username, secondContent, secondTitle);
        message.setTitle(secondTitle);
        this.mockMvc.perform(post("/admin/message").session(httpSession)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(message)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void test04GetUnreadMessageAndReadMessage() throws Exception {
        this.mockMvc.perform(get("/message/count").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").value(2));

        Long id = this.getMessage().get("unread").get(0).getId();
        this.mockMvc.perform(patch("/message/" + id + "/read").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

//        this.mockMvc.perform(patch("/message/13/read").session(httpSession))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(4001));

        this.mockMvc.perform(get("/message/count").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    public void test98DeleteMessage() throws Exception {
        Map<String, List<Message>> stringListMap = this.getMessage();
        List<Message> messageList = stringListMap.get("unread");
        messageList.addAll(stringListMap.get("read"));
        for (Message message : messageList) {
            this.mockMvc.perform(delete("/message/" + message.getId()).session(httpSession))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0));
        }

//        this.mockMvc.perform(delete("/message/13").session(httpSession))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(4001));

        this.mockMvc.perform(delete("/message/10000").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(4002));

    }

    @Test
    public void test99Logout() throws Exception {
        AuthenticationProcess.logout(mockMvc, httpSession);
    }

}
