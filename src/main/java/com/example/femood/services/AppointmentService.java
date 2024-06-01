package com.example.femood.services;

import com.example.femood.entity.Appointment;
import com.example.femood.entity.Place;
import com.example.femood.entity.Schedule;
import com.example.femood.repo.AppointmentRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentService {
    private final AppointmentRepo appointmentRepo;
    private final ScheduleService scheduleService;

    public List<Appointment> getAll(){
        return appointmentRepo.findAll();
    }
    public List<Appointment> save(String schedule,
                            String name, String inst,
                            String email, String phone,
                            String comment){
        String[] arr;
        if ((schedule.charAt(0)=='[') &&(schedule.charAt(schedule.length()-1)==']')){
            arr=schedule.substring(1, schedule.length()-1).split(", ");
        }
        else {
            arr=schedule.split(", ");
        }
//        System.out.println(schedule.substring(1, schedule.length()-2));
        ArrayList<Appointment> appointments=new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            Schedule schedule1 =scheduleService.getById(Long.parseLong(arr[i]));
            if(!schedule1.isBooked()){
                Appointment appointment=new Appointment();
                appointment.setInst(inst);
                appointment.setNameUser(name);
                appointment.setComment(comment);
                appointment.setPhoneNumber(phone);
                appointments.add(appointment);
                schedule1.setBooked(true);
                scheduleService.save(schedule1);
                appointment.setSchedule(schedule1);
            }
        }
        if(appointments.isEmpty()){
            return null;
        }
        else {
            return appointmentRepo.saveAll(appointments);
        }
    }

    public void delete(Long id) {
        Appointment appointment=appointmentRepo.getReferenceById(id);
        appointment.getSchedule().setBooked(false);
        scheduleService.save(appointment.getSchedule());
        appointmentRepo.delete(appointment);
    }

    public String getIdsScheduleByAppointmnets(List<Appointment> appointments) {
        StringBuilder ids= new StringBuilder();
        for(int i=0;i<appointments.size();i++){
            ids.append("ids=").append(appointments.get(i).getSchedule().getId());
            if(appointments.size()-1!=i){
                ids.append("&");
            }
        }
        return ids.toString();
    }


}
