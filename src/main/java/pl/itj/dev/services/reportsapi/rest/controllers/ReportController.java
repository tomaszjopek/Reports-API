package pl.itj.dev.services.reportsapi.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.itj.dev.services.reportsapi.reports.interfaces.ReportService;

import javax.sql.DataSource;
import java.sql.SQLException;

@RestController
@RequestMapping("/v1/reports")
public class ReportController {

    private final DataSource dataSource;
    private final ReportService reportService;

    @Autowired
    public ReportController(DataSource dataSource, ReportService reportService) {
        this.dataSource = dataSource;
        this.reportService = reportService;
    }


    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() throws SQLException {


        return ResponseEntity.ok("Hellow World! " + dataSource.getConnection().toString());
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> createPdfReport() {
        reportService.generatePDF();

        return ResponseEntity.ok().body(null);
    }
}
