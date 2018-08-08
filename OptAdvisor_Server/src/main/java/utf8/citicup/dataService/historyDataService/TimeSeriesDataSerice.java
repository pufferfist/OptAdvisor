package utf8.citicup.dataService.historyDataService;

import utf8.citicup.domain.historyEntity.TimeSeriesData;

public interface TimeSeriesDataSerice {
    TimeSeriesData findByLastTradeDate(String lastTradeDate);
}
