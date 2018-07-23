package utf8.citicup;

import org.apache.catalina.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.authc.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class LoginTest {

    private static Subject subject;

    @Before
    public void prepare(){
        subject= SecurityUtils.getSubject();
    }

    @Test
    public void testShiro(){
        UsernamePasswordToken token1=new UsernamePasswordToken("wang","123");
        UsernamePasswordToken token2=new UsernamePasswordToken("admin","123456");
        try {
            subject.login(token1);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(true, subject.isAuthenticated());
        subject.logout();

        try{
            subject.login(token2);
        }catch (AuthenticationException e){
            e.printStackTrace();
        }
        Assert.assertEquals(true,subject.isAuthenticated());
        subject.logout();
    }

}
