package com.college.hostel.repository;

import com.college.hostel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHostelId(Long hostelId);
    Optional<Room> findByRoomNo(String roomNo);
    List<Room> findByCapacityGreaterThan(int currentOccupancy);
    boolean existsByRoomNo(String roomNo);
}