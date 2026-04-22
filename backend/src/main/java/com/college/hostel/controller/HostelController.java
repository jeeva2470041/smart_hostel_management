package com.college.hostel.controller;

import com.college.hostel.model.Hostel;
import com.college.hostel.service.HostelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hostels")
@CrossOrigin(origins = "http://localhost:8080")
public class HostelController {

    @Autowired
    private HostelService hostelService;

    @GetMapping
    public List<Hostel> getAllHostels() {
        return hostelService.getAllHostels();
    }
}

