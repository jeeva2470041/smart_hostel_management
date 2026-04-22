// ...existing code...
package com.college.hostel.service;

import com.college.hostel.model.Admin;
import com.college.hostel.model.Student;
import com.college.hostel.model.Room;
import com.college.hostel.repository.AdminRepository;
import com.college.hostel.repository.StudentRepository;
import com.college.hostel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    public List<Student> getAllStudentsSortedByType() {
        List<Student> all = studentRepository.findAll();
        List<Student> ug = new java.util.ArrayList<>();
        List<Student> pg = new java.util.ArrayList<>();
        List<Student> mba = new java.util.ArrayList<>();
        for (Student s : all) {
            if (s instanceof com.college.hostel.model.UGStudent) ug.add(s);
            else if (s instanceof com.college.hostel.model.PGStudent) pg.add(s);
            else if (s instanceof com.college.hostel.model.MBAStudent) mba.add(s);
        }
        ug.addAll(pg);
        ug.addAll(mba);
        return ug;
    }
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
    
    public boolean approveAllocation(Long studentId, Long roomId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        
        if (studentOpt.isPresent() && roomOpt.isPresent()) {
            Student student = studentOpt.get();
            Room room = roomOpt.get();
            
            if (room.isAvailable()) {
                room.addStudent(student);
                roomRepository.save(room);
                return true;
            }
        }
        return false;
    }
    
    public void removeStudentFromRoom(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null && student.getAllocatedRoom() != null) {
            Room room = student.getAllocatedRoom();
            room.removeStudent(student);
            roomRepository.save(room);
            studentRepository.save(student);
        }
    }
    
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
}