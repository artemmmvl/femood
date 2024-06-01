package com.example.femood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private LocalDateTime date;
    @Column
    private boolean booked=false;
    @OneToOne(mappedBy = "schedule")
    private Appointment appointment;
    @ManyToOne
    private Place place;
    @Column
    private boolean actual=true;
    @Column
    private boolean hidden=false;
}
