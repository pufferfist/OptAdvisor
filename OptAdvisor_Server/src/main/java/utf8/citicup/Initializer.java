package utf8.citicup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import utf8.citicup.dataService.historyDataService.OptionTsdDataService;
import utf8.citicup.domain.entity.User;
import utf8.citicup.service.UserService;


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
}

