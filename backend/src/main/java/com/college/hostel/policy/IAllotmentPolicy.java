package com.college.hostel.policy;

import com.college.hostel.model.Room;
import com.college.hostel.model.Student;
import com.college.hostel.model.Hostel;
import com.college.hostel.exception.AllocationException;

public interface IAllotmentPolicy {
    Room allocateRoom(Student student, Hostel hostel) throws AllocationException;
}