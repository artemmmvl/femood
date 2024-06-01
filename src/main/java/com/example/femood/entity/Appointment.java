package com.example.femood.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String nameUser;
//    @Column
//    private String email;
    @Column
    private String phoneNumber;
    @Column
    private String inst;

    @OneToOne
    private Schedule schedule;
    @Column
    private String comment;
    @Column
    private LocalDateTime recordingTime=LocalDateTime.now();
}
