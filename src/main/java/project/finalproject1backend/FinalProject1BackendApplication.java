package project.finalproject1backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FinalProject1BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProject1BackendApplication.class, args);
    }

}
