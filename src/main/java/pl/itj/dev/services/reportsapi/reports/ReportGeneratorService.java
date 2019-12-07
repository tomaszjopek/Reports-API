package pl.itj.dev.services.reportsapi.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.itj.dev.services.reportsapi.reports.interfaces.ReportService;

@Service
@Slf4j
public class ReportGeneratorService implements ReportService {

    private final ReportCompileService reportCompileService;

    @Autowired
    public ReportGeneratorService(ReportCompileService reportCompileService) {
        this.reportCompileService = reportCompileService;
    }

    @Override
    public void generatePDF() {
        try {
            reportCompileService.compileAndSaveJasperReport("/report_templates/customer_events.jrxml");
        } catch (JRException e) {
            log.error("Error during compiling report.", e);
        }
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
