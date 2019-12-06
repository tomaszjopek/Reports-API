package pl.itj.dev.services.reportsapi.reports;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import pl.itj.dev.services.reportsapi.reports.interfaces.ReportService;

@Service
public class ReportGeneratorService implements ReportService {

    @Override
    public void generatePDF() {
        throw new NotImplementedException("Generate pdf method not implemented");
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
