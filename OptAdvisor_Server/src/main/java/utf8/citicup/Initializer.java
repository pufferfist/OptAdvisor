package utf8.citicup;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utf8.citicup.dataService.UserRepository;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.User;

@Component
public class Initializer implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
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
        //Option option1=new Option("id1","name1",0,0,"time",0,0,0,0,0,0,0,0,0);
        //Option option2=new Option("id1","name1",0,0,"time",0,0,0,0,0,0,0,0,0);
        User user=new User();
        user.setUsername("wyb");
        user.setPassword("wyb");
        user.setW1(1);
        user.setW2(2);
        userRepository.save(user);
        User user1=new User();
        user.setUsername("wyb");
        user.setPassword("wrh");
        user.setW1(1);
        user.setW2(2);
        userRepository.save(user);
    }

}