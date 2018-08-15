package utf8.citicup.dataService.historyDataService;

import utf8.citicup.domain.historyEntity.OptionTsd;

import java.util.List;

public interface OptionTsdDataService {
    OptionTsd findByCodeNameAndLatestDate(String codeName,String latestDate);

    List<OptionTsd> complexFind(String startDate, String endDate, boolean type, int findType);
}
