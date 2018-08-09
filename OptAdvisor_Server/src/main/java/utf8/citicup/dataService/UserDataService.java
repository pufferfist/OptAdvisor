package utf8.citicup.dataService;

import utf8.citicup.domain.entity.User;

public interface UserDataService {
    //基本操作
    public boolean addUser(User user);
    public boolean updateUser(User user);
        //因为user的主键不是生成的，所以add和update有所不同
    public User save(User user);
    public void delete(String username);
    public User findById(String username);

    //额外操作
    public boolean updatePassword(String username,String password);
}
