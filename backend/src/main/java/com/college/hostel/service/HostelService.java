package com.college.hostel.service;

import com.college.hostel.model.Hostel;
import com.college.hostel.repository.HostelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostelService {
    @Autowired
    private HostelRepository hostelRepository;

    public List<Hostel> getAllHostels() {
        return hostelRepository.findAll();
    }
}

