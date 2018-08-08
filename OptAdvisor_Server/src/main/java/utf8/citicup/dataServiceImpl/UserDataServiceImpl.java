package utf8.citicup.dataServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import utf8.citicup.dao.UserRepository;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.domain.entity.User;

@Service
public class UserDataServiceImpl implements UserDataService{

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean addUser(User user) {
        if(findById(user.getUsername())==null){
            save(user);
            return true;
        }
        return false;
    }

    @Override
    @CacheEvict(value = "user",key = "#user.username ")
    public boolean updateUser(User user) {
        if(findById(user.getUsername())!=null){
            save(user);
            return true;
        }
        return false;
    }

    @Override
    @CachePut(value = "user",key = "#user.username")
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "user")
    public void delete(String username) {
        if(userRepository.findByUsername(username)!=null) {
            userRepository.deleteById(username);
        }
    }

    @Override
    @Cacheable(value = "user")
    public User findById(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @CacheEvict(value = "user",key = "#username")
    public boolean updatePassword(String username, String password) {
        if(userRepository.findByUsername(username)==null)return false;
        userRepository.updatePassword(username,password);
        return true;
    }
}
