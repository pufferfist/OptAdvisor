package utf8.citicup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class CiticupApplication {
    public static void main(String[] args) {
        SpringApplication.run(CiticupApplication.class, args);
    }
}
