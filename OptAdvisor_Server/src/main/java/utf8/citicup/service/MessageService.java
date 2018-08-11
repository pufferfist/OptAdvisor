package utf8.citicup.service;

import utf8.citicup.domain.entity.ResponseMsg;

public interface MessageService {
    ResponseMsg setMessageRead(String username, Long id);

    ResponseMsg putMessage(String username, String message);

    ResponseMsg getMessage(String username);

    ResponseMsg getUnreadMessage(String username);
}
