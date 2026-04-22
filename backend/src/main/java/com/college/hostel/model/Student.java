package com.college.hostel.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Student extends User {

    private String regNo;
    private String phoneNumber;
    private int yearOfStudy;
    private double attendancePercentage;

    @Column(name = "application_time")
    private LocalDateTime applicationTime;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room allocatedRoom;

    private String requestedHostel;
    private String requestedRoomSharing;
    private String requestedAc;
    private String requestedBathroom;
    private String requestStatus;

    public Student() {}

    public Student(String username, String password, String email, String regNo,
                   String phoneNumber, int yearOfStudy, double attendancePercentage) {
        super(username, password, email, "STUDENT");
        this.regNo = regNo;
        this.phoneNumber = phoneNumber;
        this.yearOfStudy = yearOfStudy;
        this.attendancePercentage = attendancePercentage;
        this.applicationTime = LocalDateTime.now();
    }

    @Override
    public String getUserType() {
        return "STUDENT";
    }

    @com.fasterxml.jackson.annotation.JsonProperty("studentType")
    public String getStudentType() {
        return "UG";
    }

    // ✅ Added name getter properly inside class
    public String getName() {
        return getUsername(); // inherited from User
    }

    // ✅ Optional helper for Student-only display
    public String getDisplayInfo() {
        return String.format("%s (%s)", getName(), getRegNo());
    }

    // ----------------- Getters and Setters -----------------
    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public LocalDateTime getApplicationTime() { return applicationTime; }
    public void setApplicationTime(LocalDateTime applicationTime) { this.applicationTime = applicationTime; }

    public Room getAllocatedRoom() { return allocatedRoom; }
    public void setAllocatedRoom(Room allocatedRoom) { this.allocatedRoom = allocatedRoom; }

    public String getRequestedHostel() { return requestedHostel; }
    public void setRequestedHostel(String requestedHostel) { this.requestedHostel = requestedHostel; }

    public String getRequestedRoomSharing() { return requestedRoomSharing; }
    public void setRequestedRoomSharing(String requestedRoomSharing) { this.requestedRoomSharing = requestedRoomSharing; }

    public String getRequestedAc() { return requestedAc; }
    public void setRequestedAc(String requestedAc) { this.requestedAc = requestedAc; }

    public String getRequestedBathroom() { return requestedBathroom; }
    public void setRequestedBathroom(String requestedBathroom) { this.requestedBathroom = requestedBathroom; }

    public String getRequestStatus() { return requestStatus; }
    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }
}
