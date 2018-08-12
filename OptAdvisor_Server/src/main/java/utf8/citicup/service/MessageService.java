package utf8.citicup.service;

import utf8.citicup.domain.entity.Message;
import utf8.citicup.domain.entity.ResponseMsg;

public interface MessageService {
    ResponseMsg setMessageRead(String username, Long id);

    ResponseMsg putMessage(Message message);

    ResponseMsg getMessage(String username);

    ResponseMsg getNumberOfUnreadMessage(String username);

    ResponseMsg deleteMessage(String username, Long id);
}
