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
import utf8.citicup.dataService.historyDataService.OptionTsdDataService;
import utf8.citicup.dataService.historyDataService.TimeSeriesDataSerice;
import utf8.citicup.dataServiceImpl.UserDataServiceImpl;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Message;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;
import utf8.citicup.domain.entity.User;
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
    @Override
    public void run(String... args) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        System.out.println("initializing...");
        System.out.println("===============");
       // test();
       // initData();
        initShiro();
    }

    private void initShiro(){
        System.out.println("init login authentication");
        Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityUtils.setSecurityManager(factory.getInstance());
        System.out.println("done");
    }


    public void test(){
        for(int i=0;i<10;i++){
            System.out.println("The "+(i+1)+" time select");
            System.out.println(timeSeriesDataSerice.findByLastTradeDate("2015/2/2").getVolume());
        }
    }

/*    private void initData(){
        String res="";
        String content="";
        try {
            File file= org.springframework.util.ResourceUtils.getFile("classpath:test.txt");

            BufferedReader br=new BufferedReader(new FileReader(file));
            while((content=br.readLine())!=null){
              OptionTsd optionTsd=new OptionTsd(content);
              optionTsdDataService.save(optionTsd);
              Thread.sleep(2000);
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File path Error");
            res="FileNotFound";
            e.printStackTrace();
        } catch (IOException e) {
            res="FileNotFound";
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/


}