package pl.itj.dev.services.reportsapi.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

@Service
@Slf4j
public class ReportCompileService {

    public void compileAndSaveJasperReport(String path) throws JRException {
        JRSaver.saveObject(compileReportFromJrxml(path).get(), "C:\\Users\\Tomek\\workspace\\reports-api\\src\\main\\resources\\report_templates\\customer_events.jasper");
    }

    public Optional<JasperReport> compileReportFromJrxml(String path) throws JRException {
        File reportFile = new File("C:\\Users\\Tomek\\workspace\\reports-api\\src\\main\\resources\\report_templates\\customer_events.jrxml");

        if(reportFile.exists()) {
            try {
                return Optional.of(JasperCompileManager.compileReport(new FileInputStream(reportFile)));
            } catch (FileNotFoundException e) {
                log.error("File not found.", e);
            }
        }
        else {
            log.info("Report file does not exist.");
        }

        return Optional.empty();
    }
}
