package utf8.citicup.dataService;

import utf8.citicup.domain.entity.Option;

import java.util.List;

public interface OptionDataService {
    //基本操作
    public Option save(Option option);
    public void delete(long id);
    public Option findById(long id);
    //额外操作
    public List<Option> findByExpireTime(String expireTime);
    public List<Option> saveAll(List<Option> list);
}
