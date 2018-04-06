package pl.lukaspar.mainproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class EmployeeSkillTestsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeSkillTestsApplication.class, args);
    }
}
