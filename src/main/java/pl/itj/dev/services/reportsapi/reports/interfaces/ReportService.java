package pl.itj.dev.services.reportsapi.reports.interfaces;

import java.util.Map;
import java.util.Optional;

public interface ReportService {

    Optional<byte[]> generatePDF(String reportKey, Map<String, Object> reportParameters);

    Optional<byte[]> generateXLSX(String reportKey, Map<String, Object> reportParameters);

    Optional<byte[]> generateCSV(String reportKey, Map<String, Object> reportParameters);

}
