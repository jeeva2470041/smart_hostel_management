package com.college.hostel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HostelManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(HostelManagementApplication.class, args);
        System.out.println("✅ Spring Boot Application Started Successfully!");
        System.out.println("✅ Server running on: http://localhost:8080");
    }
}