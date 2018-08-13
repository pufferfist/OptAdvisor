package utf8.citicup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utf8.citicup.dataService.MessageDataService;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.dataService.historyDataService.OptionBasicInfoDataService;
import utf8.citicup.dataService.historyDataService.OptionTsdDataService;
import utf8.citicup.dataService.historyDataService.TimeSeriesDataSerice;

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
    @Override
    public void run(String... args) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        System.out.println("initializing...");
        System.out.println("===============");
        test();
    }

    public void test(){
//        messageDataService.updateReadStatus((long) 1,"male",false);
    }

}