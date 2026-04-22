package com.college.hostel.repository;

import com.college.hostel.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmployeeId(String employeeId);
    Optional<Admin> findByUsername(String username); // NEW
    boolean existsByUsername(String username);
}