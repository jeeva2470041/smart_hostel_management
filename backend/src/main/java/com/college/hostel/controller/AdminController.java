
package com.college.hostel.controller;

import com.college.hostel.model.Admin;
import com.college.hostel.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:8080")
public class AdminController {
    @GetMapping("/students/sorted")
    public List<com.college.hostel.model.Student> getAllStudentsSorted() {
        return adminService.getAllStudentsSortedByType();
    }
    
    @Autowired
    private AdminService adminService;
    
    @PostMapping("/allocate/{studentId}/{roomId}")
    public ResponseEntity<String> approveAllocation(@PathVariable Long studentId, 
                                                   @PathVariable Long roomId) {
        boolean success = adminService.approveAllocation(studentId, roomId);
        if (success) {
            return ResponseEntity.ok("Room allocated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to allocate room");
        }
    }
    
    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<String> removeStudentFromRoom(@PathVariable Long studentId) {
        adminService.removeStudentFromRoom(studentId);
        return ResponseEntity.ok("Student removed from room");
    }
    
    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }
    @GetMapping("/health")
public String healthCheck() {
    return "Application is running!";
}
}