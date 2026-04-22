package com.college.hostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRoomAllocationEmail(String toEmail, String studentName, String roomNo, String roomDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Room Allocation Confirmation - Hostel Management System");
            message.setText(
                "Dear " + studentName + ",\n\n" +
                "Your room allocation has been confirmed!\n\n" +
                "Room Details:\n" +
                "• Room Number: " + roomNo + "\n" +
                "• " + roomDetails + "\n\n" +
                "Please report to the hostel warden with your documents.\n\n" +
                "Best regards,\n" +
                "Hostel Management Team\n" +
                "College Hostel"
            );
            
            mailSender.send(message);
            System.out.println("✅ Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
}