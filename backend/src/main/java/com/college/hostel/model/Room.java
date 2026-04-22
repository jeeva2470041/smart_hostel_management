package com.college.hostel.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNo;

    @Enumerated(EnumType.STRING)
    private SharingType sharingType;

    @Enumerated(EnumType.STRING)
    private BathroomType bathroomType;

    @Enumerated(EnumType.STRING)
    private ACType acType;

    private int capacity;

    @OneToMany(mappedBy = "allocatedRoom")
    private List<Student> students = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "hostel_id")
    private Hostel hostel;

    public Room() {
    }

    public Room(String roomNo, SharingType sharingType, BathroomType bathroomType, ACType acType, int capacity) {
        this.roomNo = roomNo;
        this.sharingType = sharingType;
        this.bathroomType = bathroomType;
        this.acType = acType;
        this.capacity = capacity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public SharingType getSharingType() { return sharingType; }
    public void setSharingType(SharingType sharingType) { this.sharingType = sharingType; }
    public BathroomType getBathroomType() { return bathroomType; }
    public void setBathroomType(BathroomType bathroomType) { this.bathroomType = bathroomType; }
    public ACType getAcType() { return acType; }
    public void setAcType(ACType acType) { this.acType = acType; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
    public Hostel getHostel() { return hostel; }
    public void setHostel(Hostel hostel) { this.hostel = hostel; }

    // Business methods
    public boolean isAvailable() {
        return students.size() < capacity;
    }

    public void addStudent(Student student) {
        if (isAvailable()) {
            students.add(student);
            student.setAllocatedRoom(this);
        } else {
            throw new IllegalStateException("Room is already at full capacity.");
        }
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setAllocatedRoom(null);
    }
}