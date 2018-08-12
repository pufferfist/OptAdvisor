package utf8.citicup.utils;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utf8.citicup.service.util.JsonParse.objectToJsonString;

public class AuthenticationProcess {
    public static MockHttpSession login(MockMvc mockMvc, String username, String password) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        return (MockHttpSession) mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(objectToJsonString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn().getRequest().getSession();
    }

    public static void logout(MockMvc mockMvc, MockHttpSession httpSession) throws Exception {
        mockMvc.perform(post("/user/logout").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }
}
