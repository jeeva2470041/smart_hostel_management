package com.college.hostel.model;

import javax.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends User {

    private String employeeId;

    public Admin() {}

    // Overridden method from User
    @Override
    public String getUserType() {
        return "ADMIN";
    }

    // Constructor with username and password
    public Admin(String username, String password, String email, String employeeId) {
        super(username, password, email, "ADMIN");
        this.employeeId = employeeId;
    }

    // Old constructor for backward compatibility
    public Admin(String name, String regNo, String email, String phoneNumber, String employeeId) {
        super(name, "", email, "ADMIN"); // Using name as username
        this.employeeId = employeeId;
    }

    // ------------------------------------------------------
    // ✅ Method Overloading for Allocation Approval
    // ------------------------------------------------------

    // Approve allocation using a Room object
    public boolean approveAllocation(Student student, Room room) {
        if (room.isAvailable()) {
            room.addStudent(student);
            return true;
        }
        return false;
    }

    // Note: Room allocation by ID is handled by AdminService.approveAllocation()
    // which properly fetches the Room from the database.

    // Remove student from their allocated room
    public void removeStudentFromRoom(Student student) {
        if (student.getAllocatedRoom() != null) {
            student.getAllocatedRoom().removeStudent(student);
        }
    }

    // ----------------- Getters and Setters -----------------
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    // ✅ Added consistency with Student getters
    public String getName() { return getUsername(); }
    public void setName(String name) { setUsername(name); }

    public String getRegNo() { return getEmployeeId(); }
    public void setRegNo(String regNo) { setEmployeeId(regNo); }

    public String getPhoneNumber() { return ""; } // Admins don’t have phone
    public void setPhoneNumber(String phoneNumber) { /* Do nothing */ }
}
