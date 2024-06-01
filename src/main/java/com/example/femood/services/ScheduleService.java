package com.example.femood.services;

import com.example.femood.entity.Place;
import com.example.femood.entity.Schedule;
import com.example.femood.repo.AppointmentRepo;
import com.example.femood.repo.PlaceRepo;
import com.example.femood.repo.ScheduleRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepo scheduleRepo;
    private final PlaceRepo placeRepo;
    private final AppointmentRepo appointmentRepo;
    public Map<String, List<Schedule>> getAllScheduleForAppointmentByPlaceIdMap(long id){
        List<Schedule> schedules=getAllScheduleByPlaceId(id);
        List<String> months= Arrays.asList("янв","фев","мар","апр","май","июн","июл","авг","сен","окт","ноя","дек");
        Map<String, List<Schedule>> scheduleMap = new LinkedHashMap<>();
        if (schedules.isEmpty()) {
            return null;
        }
        int day = schedules.get(0).getDate().getDayOfMonth();
        List<Schedule> scheduleDay = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if (day != schedule.getDate().getDayOfMonth()) {
                String key = day + " " + months.get(scheduleDay.get(scheduleDay.size()-1).getDate().getMonthValue() - 1);
                scheduleMap.put(key, scheduleDay);
                day = schedule.getDate().getDayOfMonth();
                scheduleDay = new ArrayList<>();
            }
            scheduleDay.add(schedule);
        }
        scheduleMap.put(day + " " + months.get(schedules.get(schedules.size() - 1).getDate().getMonthValue() - 1), scheduleDay);

        return scheduleMap;
    }
    public Map<String, Map<String, List<Schedule>>> getAllScheduleMap(boolean actual){
        List<Schedule> schedules;
        if(actual){
            schedules=scheduleRepo.findByDateIsAfterOrderByDateAscPlaceAsc(LocalDateTime.now().withMinute(0).withHour(0));
        }
        else {
            schedules=scheduleRepo.findByDateIsBeforeOrderByDateAscPlaceAsc(LocalDateTime.now().withMinute(0).withHour(0));
        }
        List<String> months= Arrays.asList("янв","фев","мар","апр","май","июн","июл","авг","сен","окт","ноя","дек");
        Map<String, Map<String, List<Schedule>>> scheduleMap = new LinkedHashMap<>();
        if (schedules.isEmpty()) {
            return null;
        }
        int day = schedules.get(0).getDate().getDayOfMonth();
        LocalTime time = schedules.get(0).getDate().toLocalTime();
        Map<String, List<Schedule>>  scheduleTimeMap= new LinkedHashMap<>();
        List<Schedule>  scheduleTimeList= new ArrayList<>();

        for (Schedule schedule : schedules) {
            if (day != schedule.getDate().getDayOfMonth()) {
//                scheduleTimeList.add(schedule);

                String key=time.toString();
                scheduleTimeMap.put(key,scheduleTimeList);
                time=schedule.getDate().toLocalTime();

                String keyDay = day + " " + months.get(scheduleTimeList.get(scheduleTimeList.size()-1).getDate().getMonthValue() - 1);

                scheduleMap.put(keyDay, scheduleTimeMap);
                day = schedule.getDate().getDayOfMonth();
                scheduleTimeMap = new LinkedHashMap<>();
                scheduleTimeList=new ArrayList<>();
                scheduleTimeList.add(schedule);

            }
            else {
                if(time!=schedule.getDate().toLocalTime()){
//                    scheduleTimeList.add(schedule);
                    String key=time.toString();
                    scheduleTimeMap.put(key,scheduleTimeList);
                    time=schedule.getDate().toLocalTime();
                    scheduleTimeList=new ArrayList<>();
//                    scheduleTimeList.add(schedule);

                }
                scheduleTimeList.add(schedule);
            }

        }
        scheduleTimeMap.put(time.toString(),scheduleTimeList);
        scheduleMap.put(day + " " + months.get(schedules.get(schedules.size() - 1).getDate().getMonthValue() - 1), scheduleTimeMap);

        return scheduleMap;
    }
    public List<Schedule> getAllScheduleByPlaceId(long id){
        return scheduleRepo.findByPlaceAndBookedAndHiddenAndActualAndDateIsAfterOrderByDate(placeRepo.getReferenceById(id), false, false, true, LocalDateTime.now().plusHours(1));
    }
    private boolean findSchedule(List<Schedule> schedules, Schedule schedule){
        int start=0;
        int end=schedules.size()-1;

        while (start<=end){
            int mid=(start+end)/2;

            if(schedules.get(mid).getDate().equals(schedule.getDate())){
                return true;
            }
            if (schedules.get(mid).getDate().isBefore(schedule.getDate())) {
                start = mid + 1; // Отбрасываем левую часть массива
            } else {
                end = mid - 1; // Отбрасываем правую часть массива
            }
        }
        return false;
    }

    @Scheduled(fixedDelay = 24*60*60*1000)
    public void addScheduleForMonth(){
        List<Schedule> schedules=scheduleRepo.findByOrderByDate();
        List<Place> places=placeRepo.findAll();
        List<Schedule> newSchedules=new ArrayList<>();
        for(int day=0;day<21;day++){
            for(int hour=0;hour<13;hour++){
                for(int placeI=0;placeI<places.size();placeI++){
                    Schedule schedule=new Schedule();
                    schedule.setDate(LocalDateTime.now().plusDays(1+day).withHour(9+hour).withMinute(0).withSecond(0).withNano(0));
                    schedule.setPlace(places.get(placeI));
//                    System.out.println(schedule);
                    if(!findSchedule(schedules,schedule)){
                        newSchedules.add(schedule);
                    }
                    Schedule schedule1=new Schedule();
                    schedule1.setDate(LocalDateTime.now().plusDays(1+day).withHour(9+hour).withMinute(30).withSecond(0).withNano(0));
                    schedule1.setPlace(places.get(placeI));
//                    System.out.println(schedule);
                    if(!findSchedule(schedules,schedule1)){
                        newSchedules.add(schedule1);
                    }
                }
            }
        }
        scheduleRepo.saveAll(newSchedules);
    }
    @Scheduled(fixedDelay = 24*60*60*1000)
    public void deleteNotActualSchedule(){
        List<Schedule> schedules=scheduleRepo.findByOrderByDate();
        for(Schedule schedule:schedules) {
            if (schedule.getDate().plusDays(30).isBefore(LocalDateTime.now())) {
                deleteSchedule(schedule);
            }
        }
    }

    public Schedule getById(Long id) {
        return scheduleRepo.getReferenceById(id);
    }
    public Schedule save(Schedule schedule){
        return scheduleRepo.save(schedule);
    }

    public void hideSchedule(String schedules) {
        String[] arr;
        if ((schedules.charAt(0)=='[') &&(schedules.charAt(schedules.length()-1)==']')){
            arr=schedules.substring(1, schedules.length()-1).split(", ");
        }
        else {
            arr=schedules.split(", ");
        }
        List<Schedule> scheduleList=new ArrayList<>();
        for(String schedule:arr){
            Schedule schedule1=scheduleRepo.getReferenceById(Long.parseLong(schedule));
            schedule1.setHidden(true);
            if(schedule1.isBooked()) {
                schedule1.setBooked(false);
                appointmentRepo.delete(schedule1.getAppointment());
            }
            scheduleList.add(schedule1);
        }
        scheduleRepo.saveAll(scheduleList);
    }

    public void unmaskSchedule(String schedules) {
        String[] arr;
        if ((schedules.charAt(0)=='[') &&(schedules.charAt(schedules.length()-1)==']')){
            arr=schedules.substring(1, schedules.length()-1).split(", ");
        }
        else {
            arr=schedules.split(", ");
        }
        List<Schedule> scheduleList=new ArrayList<>();
        for(String schedule:arr){
            Schedule schedule1=scheduleRepo.getReferenceById(Long.parseLong(schedule));
            schedule1.setHidden(false);
            scheduleList.add(schedule1);
        }
        scheduleRepo.saveAll(scheduleList);
    }
    public void deleteSchedule(Schedule schedule){
//        Schedule schedule=scheduleRepo.getReferenceById(id);
        if(schedule.isBooked()) {
            schedule.setBooked(false);
            appointmentRepo.delete(schedule.getAppointment());
        }
         scheduleRepo.delete(schedule);
    }

    public String getStringTimeByScheduleId(Long[] ids) {
        StringBuilder ans= new StringBuilder();
        List<String> months= Arrays.asList("янв","фев","мар","апр","май","июн","июл","авг","сен","окт","ноя","дек");
        for(int i=0;i<ids.length;i++){
            Schedule schedule=scheduleRepo.getReferenceById(ids[i]);
            ans.append(schedule.getDate().getDayOfMonth()).append(" ").append(months.get(schedule.getDate().getMonthValue() - 1)).append(" ").append(schedule.getDate().toLocalTime());
            if(ids.length-1!=i){
                ans.append(", ");
            }
        }
        return String.valueOf(ans);
    }
}
