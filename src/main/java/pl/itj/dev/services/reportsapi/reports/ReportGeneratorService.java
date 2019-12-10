package pl.itj.dev.services.reportsapi.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.itj.dev.services.reportsapi.reports.config.ReportPropertiesConfig;
import pl.itj.dev.services.reportsapi.reports.interfaces.ReportService;

import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ReportGeneratorService implements ReportService {

    private final ReportPropertiesConfig reportPropertiesConfig;
    private final DataSource dataSource;

    @Autowired
    public ReportGeneratorService(ReportPropertiesConfig reportPropertiesConfig, DataSource dataSource) {
        this.reportPropertiesConfig = reportPropertiesConfig;
        this.dataSource = dataSource;
    }

    @Override
    public Optional<byte[]> generatePDF(String reportKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CUSTOMER_ID", 1L);

        File file = new File(reportPropertiesConfig.getJrxmlFilesLocation());
        File compiledTemplateFile = new File(file, reportKey + ".jasper");

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(compiledTemplateFile);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());

            return Optional.of(JasperExportManager.exportReportToPdf(jasperPrint));
        } catch (JRException | SQLException e) {
            log.error("Error during PDF report generation.");
        }

        return Optional.empty();
    }

    @Override
    public void generateXLSX() {
        throw new NotImplementedException("Generate xlsx method not implemented");
    }

    @Override
    public void generateCSV() {
        throw new NotImplementedException("Generate csv method not implemented");
    }
}
