package com.college.hostel.service;

import com.college.hostel.model.Student;
import com.college.hostel.model.Admin;
import com.college.hostel.model.User;
import com.college.hostel.repository.StudentRepository;
import com.college.hostel.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    public User login(String username, String password) {
        // normalize
        if (username == null || password == null) return null;
        String uname = username.trim();
        String pwd = password.trim();

        log.info("Login attempt for identifier='{}'", uname);

        // Check if user is a student (by username, then by email, then by regNo)
        Optional<Student> student = studentRepository.findByUsername(uname);
        if (!student.isPresent()) student = studentRepository.findByEmail(uname);
        if (!student.isPresent()) student = studentRepository.findByRegNo(uname);
        if (student.isPresent()) {
            boolean match = student.get().getPassword().equals(pwd);
            log.info("Student found (username='{}'), password match={}", student.get().getUsername(), match);
            if (match) return student.get();
        }

        // Check if user is an admin
        Optional<Admin> admin = adminRepository.findByUsername(uname);
        if (admin.isPresent()) {
            boolean match = admin.get().getPassword().equals(pwd);
            log.info("Admin found (username='{}'), password match={}", admin.get().getUsername(), match);
            if (match) return admin.get();
        }

        log.info("Authentication failed for identifier='{}'", uname);
        return null; // Invalid credentials
    }

    public Student registerStudent(Student student) {
        try {
            if (student == null || student.getUsername() == null) return null;
            // Check if username already exists
            if (studentRepository.findByUsername(student.getUsername()).isPresent() ||
                adminRepository.findByUsername(student.getUsername()).isPresent()) {
                return null;
            }

            // Ensure the role is set correctly for newly registered students
            student.setRole("STUDENT");

            // Trim fields for consistency
            if (student.getUsername() != null) student.setUsername(student.getUsername().trim());
            if (student.getPassword() != null) student.setPassword(student.getPassword().trim());
            if (student.getEmail() != null) student.setEmail(student.getEmail().trim());

            Student savedStudent = studentRepository.save(student);
            log.info("Registered student username='{}' id={}", savedStudent.getUsername(), savedStudent.getId());
            return savedStudent;
        } catch (Exception e) {
            log.error("Registration failed", e);
            return null;
        }
    }
}