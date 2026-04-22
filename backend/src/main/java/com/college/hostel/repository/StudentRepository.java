package com.college.hostel.repository;

import com.college.hostel.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAllocatedRoomIsNull();
    Optional<Student> findByRegNo(String regNo);
    Optional<Student> findByEmail(String email);
    List<Student> findByAllocatedRoomId(Long roomId);
    Optional<Student> findByUsername(String username); // NEW
       boolean existsByUsername(String username);
       long count();
}