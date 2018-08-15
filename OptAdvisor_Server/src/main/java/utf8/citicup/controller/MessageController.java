package utf8.citicup.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utf8.citicup.domain.entity.Message;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.MessageService;

import java.util.Map;

@RestController
@RequestMapping(value = "message", method = RequestMethod.POST)
public class MessageController {

    @Autowired
    public MessageService messageService;

    @PostMapping("setMessageRead")
    public ResponseMsg setMessageRead(@RequestBody Map<String, Object> params) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return messageService.setMessageRead(username, Long.parseLong(params.get("id").toString()));
    }

    @PostMapping("private/putMessage")
//    public ResponseMsg addMessage(@RequestBody Map<String, Object> params) {
//        return messageService.addMessage(params.get("username").toString(), params.get("message").toString());
//    }
    public ResponseMsg putMessage(@RequestBody Message message) {
        return messageService.putMessage(message);
    }

    @PostMapping("deleteMessage")
    public ResponseMsg deleteMessage(@RequestBody Map<String, Object> params) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return messageService.deleteMessage(username, Long.parseLong(params.get("id").toString()));
    }

    @PostMapping("getMessage")
    public ResponseMsg getMessage() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return messageService.getMessage(username);
    }

    @PostMapping("getNumberUnreadMessage")
    public ResponseMsg getNumberOfUnreadMessage() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return messageService.getNumberOfUnreadMessage(username);
    }
}
