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
import utf8.citicup.utils.AuthenticationProcess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utf8.citicup.utils.JsonParse.jsonStringToMap;
import static utf8.citicup.utils.JsonParse.objectToJsonString;

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
    public void test01PutMessage() throws Exception {
        Message message = new Message(username, firstContent);
        message.setTitle(firstTitle);

        this.mockMvc.perform(post("/message/private/putMessage").session(httpSession)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(message)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    private List<Message> getMessage() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/message/getMessage").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn();
        Map<String, Object> map = jsonStringToMap(mvcResult.getResponse().getContentAsString());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(map.get("data"), new TypeReference<List<Message>>() {
        });
    }

    @Test
    public void test02GetMessage() throws Exception {
        Message message = this.getMessage().get(0);
        Assert.assertEquals(message.getMessage(), firstContent);
        Assert.assertFalse(message.getReadStatus());
        Assert.assertEquals(message.getTitle(), firstTitle);
        Assert.assertEquals(message.getUsername(), username);
    }

    @Test
    public void test03PutSecondMessage() throws Exception {
        Message message = new Message(username, secondContent);
        message.setTitle(secondTitle);
        this.mockMvc.perform(post("/message/private/putMessage")
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(message)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    public void test04GetUnreadMessageAndReadMessage() throws Exception {
        this.mockMvc.perform(post("/message/getUnreadMessage").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").value(2));

        Long id = this.getMessage().get(0).getId();
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        this.mockMvc.perform(post("/message/setMessageRead").session(httpSession)
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        this.mockMvc.perform(post("/message/getUnreadMessage").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test98DeleteMessage() throws Exception {

        List<Message> messageList = this.getMessage();
        for (Message message : messageList) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", message.getId());
            this.mockMvc.perform(post("/message/private/deleteMessage")
                    .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(params)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0));
        }
    }

    @Test
    public void test99Logout() throws Exception {
        AuthenticationProcess.logout(mockMvc, httpSession);
    }

}
