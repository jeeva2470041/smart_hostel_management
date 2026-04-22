package com.college.hostel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/admin/test")
    public String testPage() {
        return "test-page";
    }
    
    // Comment out the other methods temporarily
    /*
    @GetMapping("/admin/students")
    public String showStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "admin-students";
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        long studentCount = studentRepository.count();
        model.addAttribute("studentCount", studentCount);
        return "admin-dashboard";
    }
    */
}