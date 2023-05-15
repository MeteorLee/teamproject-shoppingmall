package project.finalproject1backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server Url")}) //https사용 시 swagger사용하기 위한 cors해결법
@EnableJpaAuditing
@SpringBootApplication
public class FinalProject1BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProject1BackendApplication.class, args);
    }

}
