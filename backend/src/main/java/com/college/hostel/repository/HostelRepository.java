package com.college.hostel.repository;

import com.college.hostel.model.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostelRepository extends JpaRepository<Hostel, Long> {
    Optional<Hostel> findByHostelName(String hostelName);
    boolean existsByHostelName(String hostelName);
}