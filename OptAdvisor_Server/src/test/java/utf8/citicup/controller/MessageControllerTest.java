package utf8.citicup.controller;

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
import utf8.citicup.CiticupApplication;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

    private static final String username = "suun";
    private static final String password = "suunPassword";

    private static MockHttpSession httpSession;

    @Before
    public void login() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        httpSession = (MockHttpSession) this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(map)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(0))
                .andReturn().getRequest().getSession();
    }

    @Test
    public void test01PutMessage() throws Exception {
//        Message message = new Message(username, "first message content");
    }
}
