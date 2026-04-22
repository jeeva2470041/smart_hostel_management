package com.college.hostel.controller;

import com.college.hostel.model.Room;
import com.college.hostel.model.Student;
import com.college.hostel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "http://localhost:8080")
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }
    
    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomById(id);
        return room.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{roomId}/students")
    public List<Student> getStudentsInRoom(@PathVariable Long roomId) {
        return roomService.getStudentsInRoom(roomId);
    }
    
    @PostMapping
    public Room createRoom(@RequestBody Room room) {
        return roomService.saveRoom(room);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}