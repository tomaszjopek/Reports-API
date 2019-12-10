package pl.itj.dev.services.reportsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.itj.dev.services.reportsapi.reports.config.ReportPropertiesConfig;

@SpringBootApplication()
@EnableJpaRepositories(basePackages = "pl.itj.dev.services.reportsapi.repositories")
@ConfigurationPropertiesScan(basePackageClasses = {ReportPropertiesConfig.class})
public class ReportsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportsApiApplication.class, args);
    }

}
