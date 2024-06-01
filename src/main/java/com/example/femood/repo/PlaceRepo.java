package com.example.femood.repo;

import com.example.femood.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepo extends JpaRepository<Place, Long> {
    public List<Place> findAllByOrderByPlaceNumber();
}
