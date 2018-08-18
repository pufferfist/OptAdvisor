package utf8.citicup.dataService.historyDataService;

import utf8.citicup.domain.historyEntity.OptionTsd;

public interface OptionTsdDataService {
    OptionTsd findByCodeNameAndLatestDate(String codeName,String latestDate);
}
