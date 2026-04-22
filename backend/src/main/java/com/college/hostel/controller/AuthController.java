package com.college.hostel.controller;

import com.college.hostel.model.Student;
import com.college.hostel.model.User;
import com.college.hostel.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8080")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        // normalize input (trim)
        if (username != null) username = username.trim();
        if (password != null) password = password.trim();

    // Log attempt (do not print raw password)
    log.info("Login attempt for identifier='{}', passwordLength={}", username, password == null ? 0 : password.length());
    User user = authService.login(username, password);
        
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", user);
            response.put("role", user.getRole());
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody Map<String, Object> payload) {
        try {
            String studentType = (String) payload.get("studentType");
            Student student;
            if ("PG".equalsIgnoreCase(studentType)) {
                student = new com.college.hostel.model.PGStudent();
                ((com.college.hostel.model.PGStudent)student).setSpecialization((String) payload.getOrDefault("specialization", ""));
                ((com.college.hostel.model.PGStudent)student).setSemester(payload.get("semester") != null ? Integer.parseInt(payload.get("semester").toString()) : 1);
            } else if ("MBA".equalsIgnoreCase(studentType)) {
                student = new com.college.hostel.model.MBAStudent();
                ((com.college.hostel.model.MBAStudent)student).setStream((String) payload.getOrDefault("stream", ""));
                ((com.college.hostel.model.MBAStudent)student).setTrimester(payload.get("trimester") != null ? Integer.parseInt(payload.get("trimester").toString()) : 1);
            } else {
                student = new com.college.hostel.model.UGStudent();
                ((com.college.hostel.model.UGStudent)student).setBranch((String) payload.getOrDefault("branch", ""));
                ((com.college.hostel.model.UGStudent)student).setUgYear(payload.get("year") != null ? Integer.parseInt(payload.get("year").toString()) : 1);
            }
            // Set common fields
            student.setUsername((String) payload.get("username"));
            student.setPassword((String) payload.get("password"));
            student.setEmail((String) payload.get("email"));
            student.setRegNo((String) payload.get("regNo"));
            student.setPhoneNumber((String) payload.get("phoneNumber"));
            student.setYearOfStudy(payload.get("yearOfStudy") != null ? Integer.parseInt(payload.get("yearOfStudy").toString()) : 1);
            student.setAttendancePercentage(payload.get("attendancePercentage") != null ? Double.parseDouble(payload.get("attendancePercentage").toString()) : 0.0);

            log.info("Register attempt username='{}', email='{}'", student.getUsername(), student.getEmail());
            Student saved = authService.registerStudent(student);
            if (saved != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("user", saved);
                response.put("role", saved.getRole());
                response.put("message", "Student registered successfully");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Username already exists or registration failed");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}