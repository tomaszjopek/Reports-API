package pl.itj.dev.services.reportsapi.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import pl.itj.dev.services.reportsapi.reports.config.ReportPropertiesConfig;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class ReportCompileService {

    public static final String JRXML_EXTENSION = ".jrxml";
    public static final String JASPER_EXTENSION = ".jasper";
    private final ReportPropertiesConfig reportPropertiesConfig;

    @Autowired
    public ReportCompileService(ReportPropertiesConfig reportPropertiesConfig) {
        this.reportPropertiesConfig = reportPropertiesConfig;
    }

    @PostConstruct
    private void compileAndSaveAllTemplatesOnStartup() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Compiling report templates on startup in dir: " + reportPropertiesConfig.getJrxmlFilesLocation());
        File templatesDir = new File(reportPropertiesConfig.getJrxmlFilesLocation());

        File[]  templates = templatesDir.listFiles((dir, name) -> name.endsWith(JRXML_EXTENSION));
        if (templates != null) {
            Stream.of(templates).forEach(template -> {
                try {
                    log.info("Compiling report template: " + template.getName());
                    compileAndSaveJasperReport(template.getName().replace(JRXML_EXTENSION, ""));
                } catch (JRException e) {
                    log.error("Error during compiling report template: " + template.getName());
                }
            });
        }

        stopWatch.stop();
        log.info("Compiling all reports templates took: " + stopWatch.getTotalTimeMillis() + " [ms]");
    }

    public void compileAndSaveJasperReport(String templateName) throws JRException {
        try {
            Optional<JasperReport> jasperReport = compileReportFromJrxml(templateName);
            jasperReport.ifPresent(report -> {
                File file = new File(reportPropertiesConfig.getJrxmlFilesLocation());
                File compiledTemplateFile = new File(file, templateName + JASPER_EXTENSION);
                File templateFile = new File(file, templateName + JRXML_EXTENSION);

                if(reportPropertiesConfig.isSaveCompiledTemplate() && (templateFile.lastModified() > compiledTemplateFile.lastModified())) {
                    try {
                        JRSaver.saveObject(report, compiledTemplateFile);
                    } catch (JRException e) {
                        log.error("Cannot save compiled .jasper file." + templateName);
                    }
                }
            } );
        } catch (FileNotFoundException e) {
            log.error("Template not found: " + templateName , e);
        }
    }

    public Optional<JasperReport> compileReportFromJrxml(String templateName) throws JRException, FileNotFoundException {
        File file = new File(reportPropertiesConfig.getJrxmlFilesLocation());

        if(file.isDirectory()) {
            File templateFile = new File(file, templateName + JRXML_EXTENSION);
            if(templateFile.exists()) {
                return Optional.of(JasperCompileManager.compileReport(new FileInputStream(templateFile)));
            }
            else {
                log.warn("Given template file does not exist: " + templateName);
            }
        }
        else {
            log.warn("Templates root directory does not exist: " + reportPropertiesConfig.getJrxmlFilesLocation());
        }

        return Optional.empty();
    }
}
