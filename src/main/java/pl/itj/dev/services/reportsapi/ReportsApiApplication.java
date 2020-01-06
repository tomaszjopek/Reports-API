package pl.itj.dev.services.reportsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.itj.dev.services.reportsapi.reports.config.ReportPropertiesConfig;

@SpringBootApplication()
@EnableJpaRepositories(basePackages = "pl.itj.dev.services.reportsapi.repositories")
@ConfigurationPropertiesScan(basePackageClasses = {ReportPropertiesConfig.class})
@EnableEurekaClient
public class ReportsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportsApiApplication.class, args);
    }

}
