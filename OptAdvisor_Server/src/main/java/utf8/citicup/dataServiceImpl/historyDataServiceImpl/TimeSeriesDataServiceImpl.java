package utf8.citicup.dataServiceImpl.historyDataServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import utf8.citicup.dao.historyDao.TimeSeriesDataRepository;
import utf8.citicup.dataService.historyDataService.TimeSeriesDataSerice;
import utf8.citicup.domain.historyEntity.TimeSeriesData;

@Service
public class TimeSeriesDataServiceImpl implements TimeSeriesDataSerice{
    @Autowired
    private TimeSeriesDataRepository timeSeriesDataRepository;

    @Override
    @Cacheable(value = "timeSeriesData")
    public TimeSeriesData findByLastTradeDate(String lastTradeDate) {
        return timeSeriesDataRepository.findByLastTradeDate(lastTradeDate);
    }
}
