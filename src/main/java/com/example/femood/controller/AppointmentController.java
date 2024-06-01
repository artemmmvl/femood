package com.example.femood.controller;

import com.example.femood.entity.Appointment;
import com.example.femood.services.AppointmentService;
import com.example.femood.services.PlaceService;
import com.example.femood.services.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
public class AppointmentController {
    private final ScheduleService scheduleService;
    private final PlaceService placeService;
    private final AppointmentService appointmentService;
    @PostMapping("/appointments/new")
    public String addAppointment(Model model, String schedule,
                                 String name, String inst,
                                 String email, String phone,
                                 String comment, String place, Principal principal){
//        System.out.println(schedule);
//        System.out.println(name);
//        System.out.println(inst);
//        System.out.println(email);
//        System.out.println(phone);
//        System.out.println(comment);
//        System.out.println(place);
        List<Appointment> appointments=appointmentService.save(schedule,name, inst, email, phone, comment);
        if(appointments==null){
            return "redirect:/appointments/info?error";
        }
        else {
            String[] arr;
            if ((schedule.charAt(0)=='[') &&(schedule.charAt(schedule.length()-1)==']')){
                arr=schedule.substring(1, schedule.length()-1).split(", ");
            }
            else {
                arr=schedule.split(", ");
            }
            String ids=appointmentService.getIdsScheduleByAppointmnets(appointments);
            if(appointments.size()==arr.length){
                if(principal!=null){
                    return "redirect:/schedule/actual";
                }
                return "redirect:/appointments/info?successfully=1&place="+place+"&"+ids;
            }
            else {
                if(principal!=null){
                    return "redirect:/schedule/actual";
                }
                return "redirect:/appointments/info?successfully=0&place="+place+"&"+ids;
            }
        }

    }
    @GetMapping("/appointments/info")
    public String newAppointment(Model model,Long[] ids, Integer successfully, String place, String error)
    {
        if(error!=null){

            return "error-appointment";

        }
        for(int i=0;i<ids.length;i++){
            System.out.println(ids[i]);
        }

        model.addAttribute("place", place);
        model.addAttribute("time", scheduleService.getStringTimeByScheduleId(ids));
        model.addAttribute("successfully", successfully);

        return "successfully-appointment";
    }

     @GetMapping("/appointments/new")
    public String newAppointment(Model model, Long[] schedule,
                                 Integer place){

        if(place!=null){

            if(schedule!=null){
                for(int i=0;i<schedule.length;i++){
                    System.out.println(schedule[i]);
                }
                List<Long> scheduleList=new ArrayList<>(Arrays.stream(schedule).toList());
                model.addAttribute("schedule",scheduleList);

                model.addAttribute("place",place);

                return "add-appointment-info";
            }
            model.addAttribute("schedule",scheduleService.getAllScheduleForAppointmentByPlaceIdMap(place) );
            model.addAttribute("place",place);
            return "add-appointment-time";
        }
        else {
            model.addAttribute("place", placeService.getPlaces());
            return "new-appointment-place";

        }
    }
}
