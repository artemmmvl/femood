package com.example.femood.controller;

import com.example.femood.entity.Role;
import com.example.femood.entity.User;
import com.example.femood.services.PlaceService;
import com.example.femood.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class PlaceController {
    private final PlaceService placeService;
    private final UserService userService;
    @GetMapping("/places")
    public String getPageForPricePlaceChange(Model model, Principal principal){
        User user=userService.getUser(principal.getName());
        model.addAttribute("superadmin", user.getRoles().contains(Role.ROLE_SUPERADMIN));

        model.addAttribute("places", placeService.getPlaces());
        return "place";
    }
    @PostMapping("/places")
    public String pricePlaceChange( double[] prices){
//        System.out.println(prices);


        placeService.setPricePlaces(prices);
        return "redirect:/places";
    }
}
