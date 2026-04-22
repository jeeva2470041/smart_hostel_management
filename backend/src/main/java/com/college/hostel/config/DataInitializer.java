package com.college.hostel.config;

import com.college.hostel.model.*;
import com.college.hostel.repository.HostelRepository;
import com.college.hostel.repository.RoomRepository;
import com.college.hostel.repository.StudentRepository;
import com.college.hostel.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private HostelRepository hostelRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create demo admin if none exists
        if (adminRepository.count() == 0) {
            User admin = new Admin("admin", "admin123", "admin@hostel.com", "ADM001"); // upcast Admin -> User
            adminRepository.save((Admin) admin); // downcast to save
            System.out.println("✅ Created demo admin: admin / admin123");
        }
        
        // Create demo student if none exists
        if (studentRepository.count() == 0) {
            User student = new Student("student1", "student123", "student1@college.com", 
                                        "REG001", "1234567890", 2, 85.5); // upcast Student -> User
            studentRepository.save((Student) student); // downcast to save
            System.out.println("✅ Created demo student: student1 / student123");
        }

        // Create a default hostel if none exists
        if (hostelRepository.count() == 0) {
            Hostel hostel = new Hostel("Main Hostel", HostelType.GENTS);
            hostelRepository.save(hostel);
            
            // Create some sample rooms
            Room room1 = new Room("A101", SharingType.SINGLE, BathroomType.ATTACHED, ACType.AC, 1);
            Room room2 = new Room("A102", SharingType.THREE_SHARING, BathroomType.ATTACHED, ACType.AC, 3);
            Room room3 = new Room("B201", SharingType.THREE_SHARING, BathroomType.COMMON, ACType.NON_AC, 3);
            
            // Set hostel for each room
            room1.setHostel(hostel);
            room2.setHostel(hostel);
            room3.setHostel(hostel);
            
            // Save rooms
            roomRepository.save(room1);
            roomRepository.save(room2);
            roomRepository.save(room3);
            
            System.out.println("✅ Created sample rooms: A101, A102, B201");
        }
    }
}
