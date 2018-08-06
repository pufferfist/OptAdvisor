package utf8.citicup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CiticupApplication {

    public static void main(String[] args) {
        SpringApplication.run(CiticupApplication.class, args);
    }
}
