package utf8.citicup.dataServiceImpl;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import utf8.citicup.dao.MessageRepository;
import utf8.citicup.dataService.MessageDataService;
import utf8.citicup.domain.entity.Message;

import java.util.List;

@Service
public class MessageDataServiceImpl implements MessageDataService{
    @Autowired
    MessageRepository messageRepository;

    @Override
    @CachePut(value = "message",key = "#message.id")
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    @CacheEvict(value = "message")
    public void delete(long id) {
        if(messageRepository.findById(id).isPresent()) {
            messageRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable(value = "message")
    public Message findById(long id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public List<Message> findByUserName(String username) {
        return messageRepository.findByUserName(username);
    }

    @Override
    public List<Message> findByUserNameAnReadStatus(String username, boolean readStatus) {
        return messageRepository.findByUserNameAndReadStatus(username,readStatus);
    }


}

