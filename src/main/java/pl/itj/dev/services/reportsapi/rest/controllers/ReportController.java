package pl.itj.dev.services.reportsapi.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;

@RestController
@RequestMapping("/v1/reports")
public class ReportController {

    private final DataSource dataSource;

    @Autowired
    public ReportController(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() throws SQLException {


        return ResponseEntity.ok("Hellow World! " + dataSource.getConnection().toString());
    }


}
