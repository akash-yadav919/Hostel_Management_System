# Hostel_Management_System

A simple console-based Hostel Management System built in Java. It supports two types of users — **Students** and **Wardens** — and lets them manage complaints and room change requests through a menu-driven command-line interface.

## Features

### Student Portal
- Raise a complaint (e.g. Plumbing, Electrical) with a title/description
- View all complaints raised by the logged-in student
- Request a room change to a desired room number

### Warden Portal
- View all complaints raised by students
- Update a complaint's status (`IN_PROGRESS` / `RESOLVED`) along with resolution notes
- View and process (approve/reject) room change requests

## Architecture

The application is organized into three layers, all within a single file (`HostelManagementSystem.java`):

| Layer | Classes | Responsibility |
|---|---|---|
| **Entry Point** | `HostelManagementSystem` | Handles CLI login loop and menu navigation for both roles |
| **Domain Models** | `User` (abstract), `Student`, `Warden`, `Complaint`, `RoomRequest` | Represent core entities and their data |
| **Service Layer** | `AuthService`, `ComplaintService`, `RoomService` | Business logic: authentication, complaint handling, and room request handling |

### Class Overview

- **`User`** — abstract base class holding common user fields (ID, name, email, password, role)
- **`Student`** — extends `User`; adds a room number
- **`Warden`** — extends `User`; adds an assigned block
- **`Complaint`** — represents a complaint ticket with category, status, and resolution notes
- **`RoomRequest`** — represents a room change request with current/target room and status
- **`AuthService`** — stores users in-memory and authenticates login credentials
- **`ComplaintService`** — creates, lists, and updates complaints
- **`RoomService`** — creates, lists, and processes room change requests

## Data Storage

This is a **demo/in-memory application** — there is no database or file persistence. All data (users, complaints, room requests) is stored in memory using `HashMap`/`ArrayList` and is reset every time the program restarts.

## Prerequisites

- Java Development Kit (JDK) 8 or higher

## How to Run

1. Save the code as `HostelManagementSystem.java`
2. Compile:
   ```bash
   javac HostelManagementSystem.java
   ```
3. Run:
   ```bash
   java HostelManagementSystem
   ```

## Demo Credentials

Two users are pre-registered when the program starts:

| Role | User ID | Password | Details |
|---|---|---|---|
| Student | `S101` | `pass123` | Alex Smith, Room B-204 |
| Warden | `W201` | `admin123` | Dr. John, Block B |

## Usage Walkthrough

1. Run the program and enter a User ID (or `exit` to quit)
2. Enter the matching password
3. Depending on the role, you'll see either the **Student Portal** or **Warden Management Portal** menu
4. Follow the on-screen prompts to raise/view complaints or manage room requests
5. Select the "Logout" option to return to the login screen

### Example: Raising a Complaint (Student)
```
Enter User ID (or 'exit'): S101
Enter Password: pass123

--- Student Portal ---
1. Raise Complaint
2. View My Complaints
3. Request Room Change
4. Logout
Select Option: 1
Enter Complaint Category (Plumbing/Electrical): Plumbing
Enter Title/Details: Leaking tap in bathroom
Complaint logged! Ticket ID: CMP101
```

### Example: Updating a Complaint (Warden)
```
Enter User ID (or 'exit'): W201
Enter Password: admin123

--- Warden Management Portal ---
1. View All Complaints
2. Update Complaint Status
3. Process Room Change Requests
4. Logout
Select Option: 2
Enter Ticket ID to Update: CMP101
Enter New Status (IN_PROGRESS/RESOLVED): RESOLVED
Enter Resolution Notes: Plumber fixed the leak
Ticket updated successfully.
```

## Known Limitations

- Passwords are stored and compared as plain text (not hashed) — not suitable for production use
- No input validation beyond basic parsing (e.g. invalid menu options may throw exceptions)
- Data does not persist between runs
- Only one warden/student pair is seeded by default; add more via `AuthService.registerUser()`

## Possible Improvements

- Add persistent storage (file-based or a real database)
- Hash and salt passwords properly
- Add input validation and error handling for menu selections
- Support registering new students/wardens at runtime
- Add unit tests for the service layer
