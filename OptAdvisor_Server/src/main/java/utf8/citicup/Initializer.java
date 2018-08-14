package utf8.citicup;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utf8.citicup.dataService.MessageDataService;
import utf8.citicup.dataService.OptionDataService;
import utf8.citicup.dataService.PortfolioDataService;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.dataService.historyDataService.OptionBasicInfoDataService;
import utf8.citicup.dataService.historyDataService.OptionTsdDataService;
import utf8.citicup.dataService.historyDataService.TimeSeriesDataSerice;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.domain.entity.User;

import java.util.List;

@Component
public class Initializer implements CommandLineRunner {
    @Autowired
    private TimeSeriesDataSerice timeSeriesDataSerice;
    @Autowired
    private OptionTsdDataService optionTsdDataService;
    @Autowired
    private OptionBasicInfoDataService optionBasicInfoDataService;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private MessageDataService messageDataService;
    @Autowired
    private PortfolioDataService portfolioDataService;
    @Autowired
    private OptionDataService optionDataService;
    @Override
    public void run(String... args) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        System.out.println("initializing...");
        System.out.println("===============");
        test();
    }

    public void test(){
        List<Portfolio> portfolioList=portfolioDataService.findByUsername("male");
    }

}