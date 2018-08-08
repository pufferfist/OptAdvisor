package utf8.citicup;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utf8.citicup.dao.UserRepository;
import utf8.citicup.dataService.MessageDataService;
import utf8.citicup.dataService.OptionDataService;
import utf8.citicup.dataService.PortfolioDataService;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.dataService.historyDataService.OptionBasicInfoDataService;
import utf8.citicup.dataService.historyDataService.OptionTsdDataService;
import utf8.citicup.dataService.historyDataService.TimeSeriesDataSerice;
import utf8.citicup.dataServiceImpl.UserDataServiceImpl;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Message;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.domain.entity.User;
import utf8.citicup.domain.historyEntity.OptionBasicInfo;
import utf8.citicup.domain.historyEntity.OptionTsd;

import javax.annotation.Resource;
import java.io.*;
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
    @Override
    public void run(String... args) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        System.out.println("initializing...");
        System.out.println("===============");
        test();
        initShiro();
    }

    private void initShiro(){
        System.out.println("init login authentication");
        Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityUtils.setSecurityManager(factory.getInstance());
        System.out.println("done");
    }


    public void test(){
/*        for(int i=0;i<10;i++){
            System.out.println("The "+(i+1)+" time select");
            System.out.println(timeSeriesDataSerice.findByLastTradeDate("2015/2/2").getVolume());
            System.out.println(optionBasicInfoDataService.findByCodeName("10000012.SH").getAbbr());
            System.out.println(optionTsdDataService.findByCodeNameAndLatestDate("10000012.SH","2015/2/9"));
        }*//*
        userDataService.updatePassword("王一博","new Password");
        userDataService.delete("王瑞华");*/
    }

}