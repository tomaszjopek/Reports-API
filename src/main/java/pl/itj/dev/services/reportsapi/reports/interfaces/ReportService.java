package pl.itj.dev.services.reportsapi.reports.interfaces;

import java.util.Optional;

public interface ReportService {

    Optional<byte[]> generatePDF(String reportKey);

    Optional<byte[]> generateXLSX(String reportKey);

    Optional<byte[]> generateCSV(String reportKey);

}
