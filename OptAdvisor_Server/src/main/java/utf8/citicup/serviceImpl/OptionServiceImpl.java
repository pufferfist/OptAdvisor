package utf8.citicup.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import utf8.citicup.dataService.OptionDataService;
import utf8.citicup.domain.entity.ResponseMsg;
import utf8.citicup.service.OptionService;

public class OptionServiceImpl implements OptionService{
    @Autowired
    private OptionDataService optionDataService;
    @Override
    public ResponseMsg updateOptionByTime(String time) {
        return null;
    }
}
