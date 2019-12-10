package pl.itj.dev.services.reportsapi.rest.controllers;

import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.itj.dev.services.reportsapi.reports.ReportCompileService;
import pl.itj.dev.services.reportsapi.reports.config.ReportPropertiesConfig;
import pl.itj.dev.services.reportsapi.reports.interfaces.ReportService;

import java.sql.SQLException;

@RestController
@RequestMapping("/v1/reports")
public class ReportController {

    private final ReportService reportService;
    private final ReportPropertiesConfig reportPropertiesConfig;
    private final ReportCompileService reportCompileService;

    @Autowired
    public ReportController(ReportService reportService, ReportPropertiesConfig reportPropertiesConfig, ReportCompileService reportCompileService) {
        this.reportService = reportService;
        this.reportPropertiesConfig = reportPropertiesConfig;
        this.reportCompileService = reportCompileService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() throws SQLException, JRException {
        reportCompileService.compileAndSaveJasperReport("customer_events");

        return ResponseEntity.ok("Hellow World! " + reportPropertiesConfig.getJrxmlFilesLocation());
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> createPdfReport() {
        reportService.generatePDF();

        return ResponseEntity.ok().body(null);
    }
}
