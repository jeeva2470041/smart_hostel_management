package com.college.hostel.service;

import com.college.hostel.model.Student;
import com.college.hostel.model.Hostel;
import com.college.hostel.model.Room;
import com.college.hostel.policy.IAllotmentPolicy;
import com.college.hostel.exception.AllocationException;
import com.college.hostel.repository.RoomRepository; // ✅ ADD THIS IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllotmentManager<T extends Student> {
    
    @Autowired
    private IAllotmentPolicy allotmentPolicy;
    
    @Autowired
    private RoomRepository roomRepository;
    
    public Room allotRoom(T student, Hostel hostel) throws AllocationException {
        Room allocatedRoom = allotmentPolicy.allocateRoom(student, hostel);
        // Save the room to persist changes
        roomRepository.save(allocatedRoom);
        return allocatedRoom;
    }
    
    public void allotRooms(List<T> students, Hostel hostel) {
        for (T student : students) {
            try {
                allotRoom(student, hostel);
            } catch (AllocationException e) {
                System.err.println("Failed to allocate room for student: " + student.getRegNo() + 
                                 " - " + e.getMessage());
            }
        }
    }
}