package com.example.femood.controller;

import com.example.femood.entity.Role;
import com.example.femood.entity.User;
import com.example.femood.services.AppointmentService;
import com.example.femood.services.ScheduleService;
import com.example.femood.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class AdminController {
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final AppointmentService appointmentService;
    @GetMapping("/schedule/actual")
    public String getPageSchedule(Model model, Principal principal){
        model.addAttribute("schedules", scheduleService.getAllScheduleMap(true));
//        System.out.println(scheduleService.getAllScheduleMap(true));
        model.addAttribute("actual", true);
        User user=userService.getUser(principal.getName());

        model.addAttribute("superadmin", user.getRoles().contains(Role.ROLE_SUPERADMIN));


        return "schedule1";
    }
    @GetMapping("/admin")
    public String getProfile(Model model, Principal principal){
        User user=userService.getUser(principal.getName());
        model.addAttribute("people",user);
        model.addAttribute("superadmin", user.getRoles().contains(Role.ROLE_SUPERADMIN));
//        System.out.println(scheduleService.getAllScheduleMap(true));
        return "admin";
    }
    @GetMapping("/admin/users")
    public String getSuperAdminPage(Model model, Principal principal){
        model.addAttribute("users",userService.getUsers());
        return "superadmin";
    }
    @PostMapping("/admin/users/delete")
    public String deleteUser(Long id){
        userService.delete(id);
        return "redirect:/admin/users";
    }
//    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PostMapping("/admin/users/change")
    public String changeUser(Long id, boolean active, String email,
                             String firstname, String lastname, String password, String phoneNumber, String[] roles){

        for(String s:roles){
            System.out.println(s);
        }


        userService.changeUser(id, active, email, firstname, lastname, password, phoneNumber,roles);
        return "redirect:/admin/users";
    }
    @GetMapping("/admin/password/change")
    public String changePasswordPage(Model model, Principal principal){

//        model.addAttribute("people",userService.getUser(principal.getName()));
//        System.out.println(scheduleService.getAllScheduleMap(true));
        User user=userService.getUser(principal.getName());

        model.addAttribute("superadmin", user.getRoles().contains(Role.ROLE_SUPERADMIN));

        return "change-password";
    }
    @GetMapping("/schedule/old")
    public String getPageScheduleOld(Model model, Principal principal){
        model.addAttribute("schedules", scheduleService.getAllScheduleMap(false));
//        System.out.println(scheduleService.getAllScheduleMap(false));
        User user=userService.getUser(principal.getName());
        model.addAttribute("superadmin", user.getRoles().contains(Role.ROLE_SUPERADMIN));

        model.addAttribute("actual", false);

        return "schedule1";
    }
    @PostMapping("/schedule/delete")
    public String hideSchedule(String schedules){

//                model.addAttribute("schedules", scheduleService.getAllScheduleMap());
        System.out.println(schedules);
        scheduleService.hideSchedule(schedules);
        return "redirect:/schedule/actual";

    }
    @PostMapping("/schedule/add")
    public String unmaskSchedule(String schedules){
//                model.addAttribute("schedules", scheduleService.getAllScheduleMap());
        System.out.println(schedules);
        scheduleService.unmaskSchedule(schedules);
        return "redirect:/schedule/actual";
    }
    @PostMapping("/appointments/delete")
    public String deleteAppointment(Long id){
//                model.addAttribute("schedules", scheduleService.getAllScheduleMap());

        appointmentService.delete(id);
        return "redirect:/schedule/actual";

    }

}
