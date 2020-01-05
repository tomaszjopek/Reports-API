package pl.itj.dev.services.reportsapi.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import pl.itj.dev.services.reportsapi.logging.LoggingService;
import pl.itj.dev.services.reportsapi.reports.config.ReportPropertiesConfig;
import pl.itj.dev.services.reportsapi.reports.constants.FileExtensions;
import pl.itj.dev.services.reportsapi.reports.interfaces.ReportService;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Service
@Lazy
@Slf4j
public class ReportGeneratorService implements ReportService {

    private final ReportPropertiesConfig reportPropertiesConfig;
    private final DataSource dataSource;
    private final LoggingService loggingService;

    @Autowired
    public ReportGeneratorService(ReportPropertiesConfig reportPropertiesConfig, DataSource dataSource, LoggingService loggingService) {
        this.reportPropertiesConfig = reportPropertiesConfig;
        this.dataSource = dataSource;
        this.loggingService = loggingService;
    }

    @Override
    public Optional<byte[]> generatePDF(String reportKey, Map<String, Object> reportParameters) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        File file = new File(reportPropertiesConfig.getJrxmlFilesLocation());
        File compiledTemplateFile = new File(file, reportKey + FileExtensions.JASPER_EXTENSION);

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(compiledTemplateFile);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, dataSource.getConnection());

            stopWatch.stop();
            log.info("Generating pdf report for parameters: {}, took: {} [ms]", loggingService.logMap(reportParameters), stopWatch.getTotalTimeMillis());

            return Optional.of(JasperExportManager.exportReportToPdf(jasperPrint));
        } catch (JRException | SQLException e) {
            log.error("Error during PDF report generation.");
        }

        return Optional.empty();
    }

    @Override
    public Optional<byte[]> generateXLSX(String reportKey, Map<String, Object> reportParameters) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        File file = new File(reportPropertiesConfig.getJrxmlFilesLocation());
        File compiledTemplateFile = new File(file, reportKey + FileExtensions.JASPER_EXTENSION);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(compiledTemplateFile);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, dataSource.getConnection());

            OutputStreamExporterOutput outputStreamExporterOutput = new SimpleOutputStreamExporterOutput(outputStream);

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(outputStreamExporterOutput);

            SimpleXlsxExporterConfiguration configuration = prepareXlsxReportProperties();
            exporter.setConfiguration(configuration);

            exporter.exportReport();

            stopWatch.stop();
            log.info("Generating XLSX report for parameters: {}, took: {} [ms]", loggingService.logMap(reportParameters), stopWatch.getTotalTimeMillis());

            return Optional.of(outputStream.toByteArray());
        } catch (JRException | SQLException | IOException e) {
            log.error("Error during XLSX report generation.");
        }

        return Optional.empty();
    }

    private SimpleXlsxExporterConfiguration prepareXlsxReportProperties() {
        SimpleXlsxExporterConfiguration configuration = new SimpleXlsxExporterConfiguration();
        return configuration;
    }

    @Override
    public Optional<byte[]> generateCSV(String reportKey, Map<String, Object> reportParameters) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        File file = new File(reportPropertiesConfig.getJrxmlFilesLocation());
        File compiledTemplateFile = new File(file, reportKey + FileExtensions.JASPER_EXTENSION);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(compiledTemplateFile);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, dataSource.getConnection());

            SimpleWriterExporterOutput outputStreamExporterOutput = new SimpleWriterExporterOutput(outputStream);

            JRCsvExporter exporter = new JRCsvExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(outputStreamExporterOutput);

            SimpleCsvExporterConfiguration configuration = prepareCSVReportProperties();
            exporter.setConfiguration(configuration);

            exporter.exportReport();

            stopWatch.stop();
            log.info("Generating CSV report for parameters: {}, took: {} [ms]", loggingService.logMap(reportParameters), stopWatch.getTotalTimeMillis());

            return Optional.of(outputStream.toByteArray());
        } catch (JRException | SQLException | IOException e) {
            log.error("Error during CSV report generation.");
        }

        return Optional.empty();
    }

    private SimpleCsvExporterConfiguration prepareCSVReportProperties() {
        SimpleCsvExporterConfiguration csvConfiguration = new SimpleCsvExporterConfiguration();
        csvConfiguration.setWriteBOM(Boolean.TRUE);
        csvConfiguration.setRecordDelimiter("\r\n");

        return csvConfiguration;
    }
}
