package utf8.citicup.dataServiceImpl.historyDataServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import utf8.citicup.dao.historyDao.OptionBasicInfoRepository;
import utf8.citicup.dataService.historyDataService.OptionBasicInfoDataService;
import utf8.citicup.domain.historyEntity.OptionBasicInfo;

@Service
public class OptionBasicInfoDataServiceImpl implements OptionBasicInfoDataService {
    @Autowired
    private OptionBasicInfoRepository optionBasicInfoRepository;
    @Override
    @Cacheable(value="optionBasicInfo")
    public OptionBasicInfo findByCodeName(String codeName) {
        return optionBasicInfoRepository.findById(codeName).orElse(null);
    }
}
