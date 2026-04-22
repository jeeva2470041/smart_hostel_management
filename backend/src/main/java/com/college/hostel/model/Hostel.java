package com.college.hostel.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore; // ADD THIS IMPORT
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hostels")
public class Hostel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostelName;
    
    @Enumerated(EnumType.STRING)
    private HostelType hostelType = HostelType.MIXED;

    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // ADD THIS to break circular reference
    private List<Room> rooms = new ArrayList<>();

    public Hostel() {}

    public Hostel(String hostelName) {
        this.hostelName = hostelName;
    }

    public Hostel(String hostelName, HostelType hostelType) {
        this.hostelName = hostelName;
        this.hostelType = hostelType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getHostelName() { return hostelName; }
    public void setHostelName(String hostelName) { this.hostelName = hostelName; }
    public List<Room> getRooms() { return rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }

    public HostelType getHostelType() { return hostelType; }
    public void setHostelType(HostelType hostelType) { this.hostelType = hostelType; }

    // Business methods
    public void addRoom(Room room) {
        rooms.add(room);
        room.setHostel(this);
    }

    public Room findAvailableRoom(SharingType sharing, BathroomType bathroom, ACType ac) {
        for (Room room : rooms) {
            if (room.getSharingType() == sharing && 
                room.getBathroomType() == bathroom && 
                room.getAcType() == ac && 
                room.isAvailable()) {
                return room;
            }
        }
        return null;
    }
}