package pl.itj.dev.services.reportsapi.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.itj.dev.services.reportsapi.reports.config.ReportPropertiesConfig;
import pl.itj.dev.services.reportsapi.reports.interfaces.ReportService;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
    public Optional<byte[]> generateXLSX(String reportKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CUSTOMER_ID", 1L);

        File file = new File(reportPropertiesConfig.getJrxmlFilesLocation());
        File compiledTemplateFile = new File(file, reportKey + ".jasper");

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(compiledTemplateFile);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());

            OutputStreamExporterOutput outputStreamExporterOutput = new SimpleOutputStreamExporterOutput(outputStream);

            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(outputStreamExporterOutput);

            SimpleXlsReportConfiguration configuration = prepareXlsxReportProperties();
            exporter.setConfiguration(configuration);

            exporter.exportReport();
            return Optional.of(outputStream.toByteArray());
        } catch (JRException | SQLException | IOException e) {
            log.error("Error during PDF report generation.");
        }

        return Optional.empty();
    }

    private SimpleXlsReportConfiguration prepareXlsxReportProperties() {
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        return configuration;
    }

    @Override
    public Optional<byte[]> generateCSV(String reportKey) {
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
}
