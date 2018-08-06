package utf8.citicup.dataService;

import utf8.citicup.domain.entity.User;

public interface UserDataService {
    public User save(User user);
    public void delete(String username);
    public User findById(String username);
}
