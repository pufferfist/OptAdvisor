package utf8.citicup.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.MessageDataService;
import utf8.citicup.domain.entity.Message;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.MessageService;
import utf8.citicup.service.util.StatusMsg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDataService messageDataService;

    @Override
    public ResponseMsg setMessageRead(String username, Long id) {
        if (messageDataService.updateReadStatus(id, username, true))
            return StatusMsg.setMessageReadSuccess;
        else
            return StatusMsg.messageNotMatchUser;
    }

    public ResponseMsg putMessage(Message message) {
        message.setReadStatus(false);
        messageDataService.save(message);
        return StatusMsg.putMessageSuccess;
    }

    @Override
    public ResponseMsg getMessage(String username) {
        Map<String, List<Message>> stringMessageListMap = new HashMap<>();
        stringMessageListMap.put("read", messageDataService.findByUsernameAnReadStatus(username, true));
        stringMessageListMap.put("unread", messageDataService.findByUsernameAnReadStatus(username, false));
        return new ResponseMsg(0, "Get user's all message success", stringMessageListMap);
    }

    @Override
    public ResponseMsg getNumberOfUnreadMessage(String username) {
        return new ResponseMsg(0, "Get number of unread message success",
                messageDataService.findByUsernameAnReadStatus(username, false).size());
    }

    @Override
    public ResponseMsg deleteMessage(String username, Long id) {
        Message message = messageDataService.findById(id);
        if (null == message) {
            return StatusMsg.messageNotExists;
        } else if (message.getUsername().equals(username)) {
            messageDataService.delete(id);
            return StatusMsg.deleteMessageSuccess;
        } else {
            return StatusMsg.messageNotMatchUser;
        }
    }
}
