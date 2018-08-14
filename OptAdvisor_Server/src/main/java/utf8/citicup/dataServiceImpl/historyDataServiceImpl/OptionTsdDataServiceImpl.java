package utf8.citicup.dataServiceImpl.historyDataServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import utf8.citicup.dao.historyDao.OptionTsdRepository;
import utf8.citicup.dataService.historyDataService.OptionTsdDataService;
import utf8.citicup.domain.historyEntity.OptionTsd;

import java.util.List;

@Service
public class OptionTsdDataServiceImpl implements OptionTsdDataService{
    @Autowired
    OptionTsdRepository optionTsdRepository;

    @Override
    @Cacheable(value="optionTsdData")
    public OptionTsd findByCodeNameAndLatestDate(String codeName, String latestDate) {
        return optionTsdRepository.findByCodeNameAndLatestDate(codeName,latestDate);
    }

    @Override
    public List<OptionTsd> complexFind(String startDate, String endDate, boolean type, int findType) {
        if(findType==0){
            return optionTsdRepository.complexFindLow(startDate,endDate,type);
        }else if(findType==1){
            return optionTsdRepository.complexFindHigh(startDate,endDate,type);
        }
        return null;
    }
}
