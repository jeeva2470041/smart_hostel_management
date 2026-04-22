package com.college.hostel.policy;

import com.college.hostel.model.*;
import com.college.hostel.exception.AllocationException;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriorityAllotmentPolicy implements IAllotmentPolicy {

    @Override
    public Room allocateRoom(Student student, Hostel hostel) throws AllocationException {
        Room selectedRoom = null;

        try {
            // Get all available rooms and sort by priority
            List<Room> availableRooms = hostel.getRooms().stream()
                    .filter(Room::isAvailable)
                    .sorted(this::compareRoomsByPriority)
                    .collect(Collectors.toList());

            if (availableRooms.isEmpty()) {
                throw new AllocationException("No available rooms for student: " + student.getRegNo());
            }

            selectedRoom = availableRooms.get(0);
            selectedRoom.addStudent(student);

            System.out.println("✅ Room allocated successfully for student: " + student.getName());
        } 
        catch (AllocationException e) {
            System.err.println("❌ Allocation failed: " + e.getMessage());
            throw e; // rethrowing the exception for higher-level handling
        } 
        catch (Exception e) {
            System.err.println("⚠ Unexpected error during room allocation: " + e.getMessage());
            throw new AllocationException("Unexpected error occurred while allocating room for: " + student.getRegNo());
        } 
        finally {
            System.out.println("🔄 Room allocation process completed for student: " + student.getRegNo());
        }

        return selectedRoom;
    }

    private int compareRoomsByPriority(Room r1, Room r2) {
        int priority1 = calculateRoomPriority(r1);
        int priority2 = calculateRoomPriority(r2);

        // Higher priority rooms first
        return Integer.compare(priority2, priority1);
    }

    private int calculateRoomPriority(Room room) {
        int priority = 0;

        // Room type priority
        if (room.getSharingType() == SharingType.SINGLE) priority += 10;
        else if (room.getSharingType() == SharingType.THREE_SHARING) priority += 5;

        // AC priority
        if (room.getAcType() == ACType.AC) priority += 5;

        // Bathroom priority
        if (room.getBathroomType() == BathroomType.ATTACHED) priority += 3;

        return priority;
    }
}
