package utf8.citicup;

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
/*        Option option1=new Option();
        option1.setName("opt1");
        Option option2=new Option();
        option2.setName("opt2");
        Option[] options={option1,option2};
        Portfolio portfolio=new Portfolio("male",options, Type.DIY,false);
        portfolio=portfolioDataService.save(portfolio);

        Portfolio result=portfolioDataService.findById(portfolio.getId());
        System.out.println(result.getOptions().length);*/
    }

}