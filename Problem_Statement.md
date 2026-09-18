# 🏨 Hostel Management System — Problem Statement

## 📌 Title
**Hostel Management System** — A Java Console-Based Application for Complaint and Room Change Management

## 🧭 Background

Hostels housing large numbers of students typically rely on manual or informal processes — physical complaint registers, verbal requests, or ad-hoc spreadsheets — to track maintenance issues (plumbing, electrical, etc.) and student requests to change rooms. These manual processes are slow, error-prone, and make it difficult for wardens to track the status of pending issues or for students to know when their request will be addressed.

## ❗ Problem Statement

There is a need for a simple, structured system that allows:

- **Students** to formally log complaints and room change requests, and track their status without needing to follow up in person.
- **Wardens** to view, prioritize, and update the status of complaints and requests in one place, with a clear audit trail (ticket IDs, statuses, resolution notes).

Without such a system, complaints can be lost or forgotten, room change requests go untracked, and there is no accountability trail showing who resolved what and when. This project addresses that gap with a lightweight, role-based, terminal-driven application.

## 🎯 Objective

To design and implement a Java-based console application that:

1. Authenticates users by role (**Student** / **Warden**).
2. Allows students to raise complaints and request room changes.
3. Allows wardens to review, update, and resolve complaints and room requests.
4. Maintains a clear, auto-generated ticket/request ID system for traceability.
5. Demonstrates sound object-oriented design (abstraction, inheritance, encapsulation, layered architecture).

## 👥 Target Users

| User | Needs |
|---|---|
| 🎓 Student | Log complaints, track their status, request a room change |
| 🧑‍💼 Warden | View all complaints/requests, update statuses, approve/reject room changes |

## 🚧 Constraints

- Console-based interface only (no GUI in the current version).
- Data is stored in memory — it does not persist between program runs.
- Single-session, single-user-at-a-time interaction (no concurrent multi-user access).
- No external database or network dependency; the system is self-contained.

## ✅ Expected Outcome

A working Java application, compiled from a single source file, that:
- Lets a student log in, raise a complaint, and see its status change as a warden resolves it.
- Lets a student request a room change and see whether it was approved or rejected.
- Lets a warden view all outstanding complaints and requests and act on them.
- Demonstrates a clean separation between domain models (`User`, `Student`, `Warden`, `Complaint`, `RoomRequest`) and business logic (`AuthService`, `ComplaintService`, `RoomService`).

## 🌱 Future Scope

The current console-based solution is a proof of concept. It can be extended with persistent storage (database), a graphical or web interface, password hashing for security, and additional modules such as fee management and visitor logs — as detailed in the project report.
