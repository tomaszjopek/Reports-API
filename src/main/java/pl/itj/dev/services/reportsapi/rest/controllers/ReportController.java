package pl.itj.dev.services.reportsapi.rest.controllers;

import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.itj.dev.services.reportsapi.reports.ReportCompileService;
import pl.itj.dev.services.reportsapi.reports.config.ReportPropertiesConfig;
import pl.itj.dev.services.reportsapi.reports.interfaces.ReportService;

import java.sql.SQLException;
import java.util.Optional;

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
        return ResponseEntity.ok("Hello World! " + reportPropertiesConfig.getJrxmlFilesLocation());
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> createCustomerPdfReport() {
        Optional<byte[]> pdfReport = reportService.generatePDF("customer_events");

        return pdfReport.map(bytes -> ResponseEntity
                .ok()
                .contentLength(bytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes)).orElseGet(() -> ResponseEntity
                .ok()
                .contentLength(0L)
                .contentType(MediaType.APPLICATION_PDF)
                .body(null));
    }

    @GetMapping(value = "/xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> createCustomerXlsxReport() {
        Optional<byte[]> xlsxReport = reportService.generateXLSX("customer_events");

        return xlsxReport.map(bytes -> ResponseEntity
                .ok()
                .contentLength(bytes.length)
                .body(bytes)).orElseGet(() -> ResponseEntity
                .ok()
                .contentLength(0L)
                .body(null));
    }

    @GetMapping(value = "/csv", produces = "text/csv")
    public ResponseEntity<byte[]> createCustomerCsvReport() {
        Optional<byte[]> csvReport = reportService.generateCSV("customer_events");

        return csvReport.map(bytes -> ResponseEntity
                .ok()
                .contentLength(bytes.length)
                .body(bytes)).orElseGet(() -> ResponseEntity
                .ok()
                .contentLength(0L)
                .body(null));
    }
}
