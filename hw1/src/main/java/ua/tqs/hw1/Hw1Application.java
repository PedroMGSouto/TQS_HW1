package ua.tqs.hw1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class Hw1Application {
    public static void main(String[] args) {
        SpringApplication.run(Hw1Application.class, args);
    }

    @Bean
    public Docket mysmArthhomeAPI()    {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("ua.tqs.hw1")).build();
    }
}
//mvn clean verify sonar:sonar -Dsonar.login=81cffeb53c2a3970dfad970973dae2134c2e5df7