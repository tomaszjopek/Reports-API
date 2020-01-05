package pl.itj.dev.services.reportsapi.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoggingService {

    private List<String> parametersToLog;

    @Autowired
    public LoggingService(List<String> parametersToLog) {
        this.parametersToLog = parametersToLog;
    }

    public String logMap(Map<String, Object> map) {
        return map
                .keySet()
                .stream()
                .filter(key -> parametersToLog.contains(key))
                .map(key -> key + " = " + map.get(key))
                .collect(Collectors.joining(",", "{", "}"));
    }
}
