package utf8.citicup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "Hello Spring Boot";
    }

    @CrossOrigin
    @GetMapping("/testCrossOrigin")
    public String testCrossOrigin(){
        return "{\n" +
                "            'projectName' : 'citiCupCompetition',\n" +
                "            'get':'test'\n" +
                "        }";
    }

    @GetMapping("/")
    public String testEnter(){
        return "hello UTF-8!";
    }
}
