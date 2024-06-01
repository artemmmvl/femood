package com.example.femood.repo;

import com.example.femood.entity.Place;
import com.example.femood.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, Long> {
    public List<Schedule> findByOrderByDate();
    public List<Schedule> findByDateIsAfterOrderByDateAscPlaceAsc(LocalDateTime startTime);
    public List<Schedule> findByDateIsBeforeOrderByDateAscPlaceAsc(LocalDateTime endTime);


    public  List<Schedule> findByPlaceAndBookedAndHiddenAndActualAndDateIsAfterOrderByDate(Place place, boolean booked, boolean hidden, boolean actual, LocalDateTime start);
}
