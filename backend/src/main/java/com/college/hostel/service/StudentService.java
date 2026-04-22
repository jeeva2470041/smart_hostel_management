package com.college.hostel.service;

import com.college.hostel.model.Student;
import com.college.hostel.model.Hostel;
import com.college.hostel.model.Room;
import com.college.hostel.exception.AllocationException;
import com.college.hostel.repository.StudentRepository;
import com.college.hostel.repository.HostelRepository;
import com.college.hostel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private HostelRepository hostelRepository;

    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private AllotmentManager<Student> allotmentManager;
    
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
   @Autowired
    private EmailService emailService; // Add this
    
    public Room allocateRoomToStudent(Long studentId, Long hostelId) throws AllocationException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AllocationException("Student not found"));
        
        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new AllocationException("Hostel not found"));
        
        Room allocatedRoom = allotmentManager.allotRoom(student, hostel);
        
        // Save the student to update the relationship
        studentRepository.save(student);
        
        // Send email notification
        sendAllocationEmail(student, allocatedRoom);
        
        return allocatedRoom;
    }

    public Student allocateStudentToSpecificRoom(Long studentId, Long roomId) throws AllocationException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AllocationException("Student not found"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AllocationException("Room not found"));

        if (!room.isAvailable()) {
            throw new AllocationException("Room is not available");
        }

        room.addStudent(student);

        // Persist both sides
        roomRepository.save(room);
        studentRepository.save(student);

        sendAllocationEmail(student, room);

        // Return the updated student (fresh from DB)
        return studentRepository.findById(studentId).orElse(student);
    }

    /**
     * Save a student's room request/preferences. This does not allocate a room,
     * it just stores the student's selection for admin review.
     */
    public Student saveStudentRequest(Long studentId, Student requestPayload) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) return null;

        student.setRequestedHostel(requestPayload.getRequestedHostel());
        student.setRequestedRoomSharing(requestPayload.getRequestedRoomSharing());
        student.setRequestedAc(requestPayload.getRequestedAc());
        student.setRequestedBathroom(requestPayload.getRequestedBathroom());
        student.setRequestStatus(requestPayload.getRequestStatus() == null ? "PENDING" : requestPayload.getRequestStatus());

        return studentRepository.save(student);
    }
    
    private void sendAllocationEmail(Student student, Room room) {
        try {
            String roomDetails = String.format(
                "Type: %s | AC: %s | Bathroom: %s | Capacity: %d",
                room.getSharingType(),
                room.getAcType(),
                room.getBathroomType(),
                room.getCapacity()
            );
            
            emailService.sendRoomAllocationEmail(
                student.getEmail(),
                student.getUsername(),
                room.getRoomNo(),
                roomDetails
            );
        } catch (Exception e) {
            System.err.println("Failed to send allocation email: " + e.getMessage());
            // Don't throw exception - email failure shouldn't break allocation
        }
    }
    
    public void removeStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null && student.getAllocatedRoom() != null) {
            student.getAllocatedRoom().removeStudent(student);
            studentRepository.save(student);
        }
        studentRepository.deleteById(studentId);
    }
    
    public List<Student> getUnallocatedStudents() {
        return studentRepository.findByAllocatedRoomIsNull();
    }
} 