package de.lehrcode.burnafterreading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@EnableJdbcAuditing
@SpringBootApplication
public class BurnAfterReadingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BurnAfterReadingApplication.class, args);
    }

}
