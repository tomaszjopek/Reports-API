package pl.itj.dev.services.reportsapi.reports.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "report", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class ReportPropertiesConfig {

    private String jrxmlFilesLocation;
    private boolean saveCompiledTemplate;

    public String getJrxmlFilesLocation() {
        return jrxmlFilesLocation;
    }

    public void setJrxmlFilesLocation(String jrxmlFilesLocation) {
        this.jrxmlFilesLocation = jrxmlFilesLocation;
    }

    public boolean isSaveCompiledTemplate() {
        return saveCompiledTemplate;
    }

    public void setSaveCompiledTemplate(boolean saveCompiledTemplate) {
        this.saveCompiledTemplate = saveCompiledTemplate;
    }
}
