package utf8.citicup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.UserService;
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
import utf8.citicup.domain.historyEntity.OptionTsd;

import java.util.List;

@Component
public class Initializer implements CommandLineRunner {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(Initializer.class);

    @Override
    public void run(String... args) throws Exception {
        this.initializeAdministrator();
        System.out.println(System.getProperty("user.dir"));
        System.out.println("initializing...");
        System.out.println("===============");
//        test();
    }

    private void initializeAdministrator() {
        if (!(Boolean) userService.isUsernameUsed("suun").getData()) {
            User user = new User();
            user.setUsername("suun");
            user.setPassword("suunPassword");
            user.setName("suun");
            user.setBirthday("2000/7/9");
            user.setTelephone("17602529171");
            user.setEmail("imagecser@gmail.com");
            user.setAvatarPath("");
            user.setGender("male");
            user.setW1(70);
            user.setW2(30);
            userService.signUp(user);
            this.logger.info("Administrator user information posted");
        }
    }


    public void test(){
//        List<OptionTsd> optionTsds=optionTsdDataService.complexFind("2015/2/9","2015/3/25",true,0);
//        List<OptionTsd> optionTsds1=optionTsdDataService.complexFind("2015/2/9","2015/3/25",true,1);
//
//        for(OptionTsd each:optionTsds){
//            System.out.println(each.getCodeName());
//        }
//        System.out.println("-------------");
//        for(OptionTsd each:optionTsds1){
//            System.out.println(each.getCodeName());
//        }


}

}
