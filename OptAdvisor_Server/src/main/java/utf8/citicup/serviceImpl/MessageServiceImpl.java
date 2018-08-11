package utf8.citicup.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utf8.citicup.dataService.MessageDataService;
import utf8.citicup.domain.entity.Message;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.MessageService;
import utf8.citicup.service.util.StatusMsg;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDataService messageDataService;

    @Override
    public ResponseMsg setMessageRead(String username, Long id) {
        if (messageDataService.updateReadStatus(id, username, true))
            return StatusMsg.setMessageReadSuccess;
        else
            return StatusMsg.MessageNotMatchUser;
    }

    public ResponseMsg putMessage(String username, String message) {
        messageDataService.save(new Message(username, message));
        return StatusMsg.putMessageSuccess;
    }

    @Override
    public ResponseMsg getMessage(String username) {
        return new ResponseMsg(0, "Get user's all message success", messageDataService.findByUsername(username));
    }

    @Override
    public ResponseMsg getUnreadMessage(String username) {
        return new ResponseMsg(0, "Get number of unread message success",
                messageDataService.findByUsernameAnReadStatus(username, false).size());
    }

    @Override
    public ResponseMsg deleteMessage(Long id) {
        messageDataService.delete(id);
        return StatusMsg.deleteMessageSuccess;
    }
}
