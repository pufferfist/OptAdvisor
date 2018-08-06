package utf8.citicup.dataService;

import utf8.citicup.domain.entity.Message;

public interface MessageDataService {
    public Message save(Message message);
    public void delete(long id);
    public Message findById(long id);
}
