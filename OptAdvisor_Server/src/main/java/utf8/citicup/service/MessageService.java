package utf8.citicup.service;

import utf8.citicup.domain.entity.ResponseMsg;

public interface MessageService {
    ResponseMsg getMessage(String username);

    ResponseMsg getUnreadMessage(String username);
}
