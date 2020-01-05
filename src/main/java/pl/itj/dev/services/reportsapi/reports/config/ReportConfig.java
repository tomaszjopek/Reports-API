package pl.itj.dev.services.reportsapi.reports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.itj.dev.services.reportsapi.reports.constants.ReportParameters;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "pl.itj.dev.services.reportsapi.reports")
public class ReportConfig {

    @Bean
    public List<String> parametersToLog() {
        return List.of(ReportParameters.CUSTOMER_ID_PARAM);
    }

}
