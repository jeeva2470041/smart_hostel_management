# Smart Hostel Management System

A comprehensive hostel management system designed to streamline room allocation, student management, and administrative operations. The system features a modern dark-themed interface with real-time data integration between a Spring Boot backend and a vanilla JavaScript frontend.

## Project Overview

This application provides a dual-dashboard experience for students and administrators. It automates the room allotment process based on student preferences and availability, while offering administrators full control over room categories, student profiles, and occupancy status.

## Key Features

- Admin Dashboard: Real-time statistics, room management, student tracking, and manual room allocation.
- Student Dashboard: Multi-step room selection wizard, preference submission, and current allocation status tracking.
- Automated Allotment: Logic for priority-based room assignment based on sharing type, AC status, and bathroom preferences.
- Modern UI: Fully responsive dark-mode design with glassmorphism effects and smooth transitions.
- Email Notifications: Automated email alerts for room allocation status changes.
- Student Classification: Support for UG, PG, and MBA student types with specific attributes.

## Technology Stack

- Backend: Java Spring Boot, JPA, Hibernate
- Database: H2 (File-based storage)
- Frontend: HTML5, CSS3, JavaScript (Vanilla), Bootstrap 5
- Design: Custom CSS Glassmorphism and Modern Dark Theme
- Build Tool: Maven

## Installation and Setup

1. Prerequisites:
   - Java JDK 17 or higher
   - Maven 3.6 or higher
   - Git

2. Clone and Navigate:
   ```bash
   git clone https://github.com/jeeva2470041/smart_hostel_management.git
   cd smart_hostel_management/backend
   ```

3. Build and Run:
   ```bash
   mvn spring-boot:run
   ```

4. Access the Application:
   Once the server starts, navigate to `http://localhost:8080` in your web browser.

## Usage and Credentials

The system comes pre-initialized with demo accounts for testing.

### Admin Access
- Username: admin
- Password: admin123

### Student Access
- Username: student1
- Password: student123

## Project Structure

- /backend: Contains the Spring Boot source code and resources.
- /backend/src/main/resources/static: Contains all frontend assets (HTML, CSS, JS).

---
Developed for streamlined hostel operations.
