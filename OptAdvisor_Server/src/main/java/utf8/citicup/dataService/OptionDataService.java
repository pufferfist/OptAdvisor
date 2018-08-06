package utf8.citicup.dataService;

import utf8.citicup.domain.entity.Option;

import java.util.List;

public interface OptionDataService {
    public Option save(Option option);
    public void delete(long id);
    public Option findById(long id);
    public List<Option> findByParentId(long parentId);
}
