# RedPulse Blood Donation Platform

RedPulse is a JavaFX-based blood donation management platform developed to connect blood donors and recipients through a simple, organized, and user-friendly system. The platform supports donor registration, user authentication, blood request posting, profile management, notifications, and role-based access control for administrators and moderators.

## Overview

This project is designed to streamline the process of blood donation coordination by creating a centralized platform where users can register, manage their profiles, post blood requests, and interact through a structured interface. It also includes admin and moderator functionality for managing users and posts efficiently.

## Features

- User registration and login
- Blood donor profile management
- Blood request posting
- Notification system
- Admin panel for managing users and posts
- Moderator role assignment and control
- Role-based access management
- JSON-based local data storage
- JavaFX graphical user interface

## User Roles

### Admin
- Full system access
- Can manage users and posts
- Can assign or remove moderator roles

### Moderator
- Can access management features assigned by admin
- Can help manage platform activity

### User
- Can register and log in
- Can manage personal profile
- Can post blood requests
- Can use platform features based on standard access

## Tech Stack

- Java
- JavaFX
- FXML
- Maven
- Gson
- JSON

## Run the Project
```bash
mvn javafx:run

## Project Structure

```bash
src/
 └── main/
     ├── java/
     │   └── com/example/projectredpulsenew/
     └── resources/
         └── com/example/projectredpulsenew/


