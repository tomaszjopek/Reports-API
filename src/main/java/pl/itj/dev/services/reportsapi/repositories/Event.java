package pl.itj.dev.services.reportsapi.repositories;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType;
    private double quantity;
    private double cost;

    @ManyToOne
    private Customer customer;
}
