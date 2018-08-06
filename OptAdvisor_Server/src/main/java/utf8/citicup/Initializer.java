package utf8.citicup;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
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
import utf8.citicup.dataServiceImpl.UserDataServiceImpl;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Message;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.domain.entity.User;

import java.util.List;

@Component
public class Initializer implements CommandLineRunner {
    @Autowired
    UserDataService userDataService;
    @Autowired
    MessageDataService messageDataService;
    @Autowired
    OptionDataService optionDataService;
    @Autowired
    PortfolioDataService portfolioDataService;
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
        User user=new User();
        user.setUsername("wyb");
        user.setPassword("wrh");
        user.setW2(1);
        user.setW1(3);
        System.out.println(userDataService.addUser(user));
        System.out.println(userDataService.updateUser(user));
    }

}