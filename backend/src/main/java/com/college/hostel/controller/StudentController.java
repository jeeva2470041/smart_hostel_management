package com.college.hostel.controller;

import com.college.hostel.model.Student;
import com.college.hostel.model.Room;
import com.college.hostel.exception.AllocationException;
import com.college.hostel.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:8080")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        Student savedStudent = studentService.saveStudent(student);
        return ResponseEntity.ok(savedStudent);
    }
    
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{studentId}/allocate/{hostelId}")
    public ResponseEntity<?> allocateRoom(@PathVariable Long studentId, 
                                        @PathVariable Long hostelId) {
        try {
            Room allocatedRoom = studentService.allocateRoomToStudent(studentId, hostelId);
            return ResponseEntity.ok(allocatedRoom);
        } catch (AllocationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{studentId}/allocate/room/{roomId}")
    public ResponseEntity<?> allocateStudentToRoom(@PathVariable Long studentId,
                                                   @PathVariable Long roomId) {
        try {
            Student updated = studentService.allocateStudentToSpecificRoom(studentId, roomId);
            return ResponseEntity.ok(updated);
        } catch (AllocationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/unallocated")
    public List<Student> getUnallocatedStudents() {
        return studentService.getUnallocatedStudents();
    }

    @PostMapping("/{studentId}/request")
    public ResponseEntity<?> saveStudentRequest(@PathVariable Long studentId, @RequestBody Map<String, Object> payload) {
        // Log payload for debugging
        System.out.println("Received student request payload for id=" + studentId + ": " + payload);

        // Map incoming JSON to a lightweight Student request object
        Student req = new Student();
        if (payload.containsKey("requestedHostel")) req.setRequestedHostel((String) payload.get("requestedHostel"));
        if (payload.containsKey("requestedRoomSharing")) req.setRequestedRoomSharing((String) payload.get("requestedRoomSharing"));
        if (payload.containsKey("requestedAc")) req.setRequestedAc((String) payload.get("requestedAc"));
        if (payload.containsKey("requestedBathroom")) req.setRequestedBathroom((String) payload.get("requestedBathroom"));
        if (payload.containsKey("requestStatus")) req.setRequestStatus((String) payload.get("requestStatus"));

        Student updated = studentService.saveStudentRequest(studentId, req);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }
}