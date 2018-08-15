package utf8.citicup.dataService;

import utf8.citicup.domain.entity.Message;

import java.util.List;


public interface MessageDataService {
    //基本操作
    public Message save(Message message);

    public void delete(long id);

    public Message findById(long id);
    //额外操作
    public List<Message> findByUsername(String username);

    public List<Message> findByUsernameAnReadStatus(String username,boolean readStatus);

    public boolean updateReadStatus(Long id,String username,boolean readStatus);
}