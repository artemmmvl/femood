package com.example.femood.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.util.List;

@Entity
@Getter
@Setter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private short placeNumber;
    @Column
    private String description;
    @Column
    private double price;
    @OneToMany(mappedBy = "place")
    private List<Schedule> schedules;

    @Override
    public String toString() {
        return "Place{" +
                "placeNumber=" + placeNumber +
                '}';
    }
}
