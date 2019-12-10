package pl.itj.dev.services.reportsapi.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.itj.dev.services.reportsapi.reports.config.ReportPropertiesConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

@Service
@Slf4j
public class ReportCompileService {

    private final ReportPropertiesConfig reportPropertiesConfig;

    @Autowired
    public ReportCompileService(ReportPropertiesConfig reportPropertiesConfig) {
        this.reportPropertiesConfig = reportPropertiesConfig;
    }

    public void compileAndSaveJasperReport(String templateName) throws JRException {
        try {
            Optional<JasperReport> jasperReport = compileReportFromJrxml(templateName);
            jasperReport.ifPresent(report -> {
                if(reportPropertiesConfig.isSaveCompiledTemplate()) {
                    File file = new File(reportPropertiesConfig.getJrxmlFilesLocation());
                    File savedTemplateFile = new File(file, templateName + ".jasper");
                    try {
                        JRSaver.saveObject(report, savedTemplateFile);
                    } catch (JRException e) {
                        log.error("Cannot save compiled .jasper file.");
                    }
                }
            } );
        } catch (FileNotFoundException e) {
            log.error("Template not found.", e);
        }
    }

    public Optional<JasperReport> compileReportFromJrxml(String templateName) throws JRException, FileNotFoundException {
        File file = new File(reportPropertiesConfig.getJrxmlFilesLocation());

        if(file.isDirectory()) {
            File templateFile = new File(file, templateName + ".jrxml");
            if(templateFile.exists()) {
                return Optional.of(JasperCompileManager.compileReport(new FileInputStream(templateFile)));
            }
            else {
                log.warn("Given template file does not exist.");
            }
        }
        else {
            log.warn("Templates root directory does not exist.");
        }

        return Optional.empty();
    }
}
