package utf8.citicup.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.MessageService;

@RestController
@RequestMapping(value = "message", method = RequestMethod.POST)
public class MessageController {

    @Autowired
    public MessageService messageService;

    @PostMapping("getMessage")
    public ResponseMsg getMessage() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return messageService.getMessage(username);
    }

    @PostMapping("getUnreadMessage")
    public ResponseMsg getUnreadMessage() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return messageService.getUnreadMessage(username);
    }
}
