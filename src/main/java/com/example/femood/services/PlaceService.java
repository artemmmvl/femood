package com.example.femood.services;

import com.example.femood.entity.Place;
import com.example.femood.repo.PlaceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlaceService {
    private final PlaceRepo placeRepo;
    public List<Place> getPlaces(){
        return placeRepo.findAllByOrderByPlaceNumber();
    }
    public List<Place> setPricePlaces(double[] prices){
        List<Place> places=getPlaces();
        for(int i=0;i<10;i++){
            places.get(i).setPrice(prices[i]);
        }
        return placeRepo.saveAll(places);
    }
}
