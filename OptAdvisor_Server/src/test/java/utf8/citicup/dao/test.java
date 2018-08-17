package utf8.citicup.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import utf8.citicup.domain.common.Type;
import utf8.citicup.domain.entity.Option;
import utf8.citicup.domain.entity.Portfolio;

import java.util.ArrayList;

@SpringBootTest
@RunWith(SpringRunner.class)
public class test {

    @Autowired PortfolioRepository portfolioRepository;
    @Autowired OptionRepository optionRepository;

    @Test
    public void test(){
        Option o1=new Option();
        Option o2=new Option();
        ArrayList<Option> options=new ArrayList<>();
        options.add(o1);
        options.add(o2);
        Portfolio portfolio=new Portfolio("123",options,Type.DIY,false);
        portfolioRepository.saveAndFlush(portfolio);
    }
}
