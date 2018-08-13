package utf8.citicup.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utf8.citicup.domain.entity.Message;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.MessageService;

@RestController
public class MessageV2Controller {

    @Autowired
    public MessageService messageService;

    @PutMapping("message/{id}/read")
    public ResponseMsg setMessageRead(@PathVariable Long id) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return messageService.setMessageRead(username, id);
    }

    @PostMapping("admin/message")
    public ResponseMsg addMessage(@RequestBody Message message) {
        return messageService.putMessage(message);
    }

    @GetMapping("message")
    public ResponseMsg getMessage() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return messageService.getMessage(username);
    }

    @DeleteMapping("message/{id}")
    public ResponseMsg deleteMessage(@PathVariable long id) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return messageService.deleteMessage(username, id);
    }

    @GetMapping("message/count")
    public ResponseMsg getNumberOfUnreadMessage() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return messageService.getNumberOfUnreadMessage(username);
    }
}
