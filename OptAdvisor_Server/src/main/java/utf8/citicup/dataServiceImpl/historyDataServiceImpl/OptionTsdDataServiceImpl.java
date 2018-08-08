package utf8.citicup.dataServiceImpl.historyDataServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utf8.citicup.dao.historyDao.OptionTsdRepository;
import utf8.citicup.dataService.historyDataService.OptionTsdDataService;
import utf8.citicup.domain.historyEntity.OptionTsd;

@Service
public class OptionTsdDataServiceImpl implements OptionTsdDataService{
    @Autowired
    OptionTsdRepository optionTsdRepository;

    @Override
    public OptionTsd findByCodeNameAndLatestDate(String codeName, String latestDate) {
        return optionTsdRepository.findByCodeNameAndLatestDate(codeName,latestDate);
    }
}
