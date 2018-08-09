package utf8.citicup.serviceImpl;

import org.springframework.stereotype.Service;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public ResponseMsg getMessage(String username) {
        return null;
    }

    @Override
    public ResponseMsg getUnreadMessage(String username) {
        return null;
    }
}
