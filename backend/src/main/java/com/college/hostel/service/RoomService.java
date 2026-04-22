package com.college.hostel.service;

import com.college.hostel.model.Room;
import com.college.hostel.model.Student;
import com.college.hostel.repository.RoomRepository;
import com.college.hostel.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    public List<Room> getAvailableRooms() {
        return roomRepository.findAll().stream()
                .filter(Room::isAvailable)
                .collect(java.util.stream.Collectors.toList());
    }
    
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }
    
    public List<Student> getStudentsInRoom(Long roomId) {
        return studentRepository.findByAllocatedRoomId(roomId);
    }
    
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }
    
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}