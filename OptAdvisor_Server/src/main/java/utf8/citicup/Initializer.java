package utf8.citicup;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utf8.citicup.dataService.MessageDataService;
import utf8.citicup.dataService.UserDataService;
import utf8.citicup.dataService.historyDataService.OptionBasicInfoDataService;
import utf8.citicup.dataService.historyDataService.OptionTsdDataService;
import utf8.citicup.dataService.historyDataService.TimeSeriesDataSerice;
import utf8.citicup.domain.entity.Message;

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
/*        List<Message> list1=messageDataService.findByUserName("male");
        List<Message> list2=messageDataService.findByUserNameAnReadStatus("male",false);
        for(Message message:list1){
            System.out.println("list1:"+message.getMessage());
        }
        for(Message message:list2){
            System.out.println("list2:"+message.getMessage());
        }*/
    }

}