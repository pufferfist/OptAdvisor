package utf8.citicup.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import utf8.citicup.restService.RestUserService;
import utf8.citicup.restService.exception.RestInvalidRequestException;

@Controller
@RequestMapping("api/")
public class RestUserController {
    @Autowired
    private RestUserService service;

    @PostMapping("user")
    public ResponseEntity<String> signUp() throws RestInvalidRequestException {
        service.signUp("suun", "suunPassword");
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
