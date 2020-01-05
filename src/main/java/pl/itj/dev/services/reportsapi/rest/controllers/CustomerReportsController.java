package pl.itj.dev.services.reportsapi.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.itj.dev.services.reportsapi.reports.constants.ReportParameters;
import pl.itj.dev.services.reportsapi.reports.interfaces.ReportService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/reports/customer")
public class CustomerReportsController {

    private final ReportService reportService;

    @Autowired
    public CustomerReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/{customerId}/pdf/{reportName}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> createCustomerPdfReport(@PathVariable Long customerId, @PathVariable String reportName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ReportParameters.CUSTOMER_ID_PARAM, customerId);

        Optional<byte[]> pdfReport = reportService.generatePDF(reportName, parameters);

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

    @GetMapping(value = "/{customerId}/xlsx/{reportName}", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> createCustomerXlsxReport(@PathVariable Long customerId, @PathVariable String reportName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ReportParameters.CUSTOMER_ID_PARAM, customerId);

        Optional<byte[]> xlsxReport = reportService.generateXLSX(reportName, parameters);

        return xlsxReport.map(bytes -> ResponseEntity
                .ok()
                .contentLength(bytes.length)
                .body(bytes)).orElseGet(() -> ResponseEntity
                .ok()
                .contentLength(0L)
                .body(null));
    }

    @GetMapping(value = "/{customerId}/csv/{reportName}", produces = "text/csv")
    public ResponseEntity<byte[]> createCustomerCsvReport(@PathVariable Long customerId, @PathVariable String reportName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ReportParameters.CUSTOMER_ID_PARAM, customerId);

        Optional<byte[]> csvReport = reportService.generateCSV(reportName, parameters);

        return csvReport.map(bytes -> ResponseEntity
                .ok()
                .contentLength(bytes.length)
                .body(bytes)).orElseGet(() -> ResponseEntity
                .ok()
                .contentLength(0L)
                .body(null));
    }
}
