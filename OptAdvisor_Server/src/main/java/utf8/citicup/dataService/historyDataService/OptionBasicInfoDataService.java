package utf8.citicup.dataService.historyDataService;

import utf8.citicup.domain.historyEntity.OptionBasicInfo;

public interface OptionBasicInfoDataService {
    OptionBasicInfo findByCodeName(String codeName);
}
