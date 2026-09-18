import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

// ==========================================
// MAIN CLASS (Entry Point)
// ==========================================
public class HostelManagementSystem {
    private static AuthService authService = new AuthService();
    private static ComplaintService complaintService = new ComplaintService();
    private static RoomService roomService = new RoomService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Welcome to Hostel Management System (VITyarthi) ");
        System.out.println("==================================================");

        while (true) {
            System.out.print("\nEnter User ID (or 'exit'): ");
            String id = scanner.nextLine();
            if (id.equalsIgnoreCase("exit")) break;

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            User user = authService.authenticate(id, password);
            if (user == null) {
                System.out.println("Invalid Credentials! Try S101/pass123 or W201/admin123");
                continue;
            }

            System.out.println("\nLogin Successful. Role: " + user.getRole());
            if (user instanceof Student) {
                handleStudentMenu((Student) user);
            } else if (user instanceof Warden) {
                handleWardenMenu((Warden) user);
            }
        }
        System.out.println("System exited cleanly.");
    }

    private static void handleStudentMenu(Student student) {
        while (true) {
            System.out.println("\n--- Student Portal ---");
            System.out.println("1. Raise Complaint");
            System.out.println("2. View My Complaints");
            System.out.println("3. Request Room Change");
            System.out.println("4. Logout");
            System.out.print("Select Option: ");
            int opt = Integer.parseInt(scanner.nextLine());

            if (opt == 1) {
                System.out.print("Enter Complaint Category (Plumbing/Electrical): ");
                String cat = scanner.nextLine();
                System.out.print("Enter Title/Details: ");
                String title = scanner.nextLine();
                Complaint c = complaintService.createComplaint(student.getUserId(), title, cat);
                System.out.println("Complaint logged! Ticket ID: " + c.getComplaintId());
            } else if (opt == 2) {
                // FIXED: Changed System::println to System.out::println
                complaintService.getComplaintsByStudent(student.getUserId()).forEach(System.out::println);
            } else if (opt == 3) {
                System.out.print("Enter Desired Room Number: ");
                String target = scanner.nextLine();
                RoomRequest req = roomService.createRequest(student.getUserId(), student.getRoomNumber(), target);
                System.out.println("Room request created! Request ID: " + req.getRequestId());
            } else break;
        }
    }

    private static void handleWardenMenu(Warden warden) {
        while (true) {
            System.out.println("\n--- Warden Management Portal ---");
            System.out.println("1. View All Complaints");
            System.out.println("2. Update Complaint Status");
            System.out.println("3. Process Room Change Requests");
            System.out.println("4. Logout");
            System.out.print("Select Option: ");
            int opt = Integer.parseInt(scanner.nextLine());

            if (opt == 1) {
                // FIXED: Changed System::println to System.out::println
                complaintService.getAllComplaints().forEach(System.out::println);
            } else if (opt == 2) {
                System.out.print("Enter Ticket ID to Update: ");
                String ticket = scanner.nextLine();
                System.out.print("Enter New Status (IN_PROGRESS/RESOLVED): ");
                String status = scanner.nextLine();
                System.out.print("Enter Resolution Notes: ");
                String notes = scanner.nextLine();
                if (complaintService.updateComplaintStatus(ticket, status, notes)) {
                    System.out.println("Ticket updated successfully.");
                } else System.out.println("Ticket ID not found.");
            } else if (opt == 3) {
                // FIXED: Changed System::println to System.out::println
                roomService.getAllRequests().forEach(System.out::println);
                System.out.print("Enter Request ID: ");
                String reqId = scanner.nextLine();
                System.out.print("Approve? (true/false): ");
                boolean approve = Boolean.parseBoolean(scanner.nextLine());
                if (roomService.processRequest(reqId, approve)) {
                    System.out.println("Room request processed.");
                } else System.out.println("Request ID not found.");
            } else break;
        }
    }
}

// ==========================================
// DOMAIN MODELS
// ==========================================
abstract class User {
    private String userId;
    private String name;
    private String email;
    private String passwordHash;
    private String role;

    public User(String userId, String name, String email, String passwordHash, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
}

class Student extends User {
    private String roomNumber;

    public Student(String userId, String name, String email, String passwordHash, String roomNumber) {
        super(userId, name, email, passwordHash, "STUDENT");
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}

class Warden extends User {
    private String blockAssigned;

    public Warden(String userId, String name, String email, String passwordHash, String blockAssigned) {
        super(userId, name, email, passwordHash, "WARDEN");
        this.blockAssigned = blockAssigned;
    }

    public String getBlockAssigned() { return blockAssigned; }
}

class Complaint {
    private String complaintId;
    private String studentId;
    private String title;
    private String category;
    private String status;
    private String resolutionNotes;

    public Complaint(String complaintId, String studentId, String title, String category) {
        this.complaintId = complaintId;
        this.studentId = studentId;
        this.title = title;
        this.category = category;
        this.status = "PENDING";
        this.resolutionNotes = "None";
    }

    public String getComplaintId() { return complaintId; }
    public String getStudentId() { return studentId; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String notes) { this.resolutionNotes = notes; }

    @Override
    public String toString() {
        return String.format("[%s] Category: %s | Title: %s | Status: %s | Notes: %s",
                complaintId, category, title, status, resolutionNotes);
    }
}

class RoomRequest {
    private String requestId;
    private String studentId;
    private String currentRoom;
    private String targetRoom;
    private String status;

    public RoomRequest(String requestId, String studentId, String currentRoom, String targetRoom) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.currentRoom = currentRoom;
        this.targetRoom = targetRoom;
        this.status = "PENDING";
    }

    public String getRequestId() { return requestId; }
    public String getStudentId() { return studentId; }
    public String getCurrentRoom() { return currentRoom; }
    public String getTargetRoom() { return targetRoom; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[%s] Student: %s | From: %s -> To: %s | Status: %s",
                requestId, studentId, currentRoom, targetRoom, status);
    }
}

// ==========================================
// SERVICE LAYER
// ==========================================
class AuthService {
    private Map<String, User> userDatabase = new HashMap<>();

    public AuthService() {
        registerUser(new Student("S101", "Alex Smith", "alex@vit.ac.in", "pass123", "B-204"));
        registerUser(new Warden("W201", "Dr. John", "john@vit.ac.in", "admin123", "Block B"));
    }

    public void registerUser(User user) {
        userDatabase.put(user.getUserId(), user);
    }

    public User authenticate(String userId, String password) {
        User user = userDatabase.get(userId);
        if (user != null && user.getPasswordHash().equals(password)) {
            return user;
        }
        return null;
    }
}

class ComplaintService {
    private List<Complaint> complaints = new ArrayList<>();
    private int counter = 1;

    public Complaint createComplaint(String studentId, String title, String category) {
        String id = "CMP" + (100 + counter++);
        Complaint complaint = new Complaint(id, studentId, title, category);
        complaints.add(complaint);
        return complaint;
    }

    public List<Complaint> getComplaintsByStudent(String studentId) {
        return complaints.stream()
                .filter(c -> c.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<Complaint> getAllComplaints() {
        return complaints;
    }

    public boolean updateComplaintStatus(String complaintId, String newStatus, String notes) {
        for (Complaint c : complaints) {
            if (c.getComplaintId().equalsIgnoreCase(complaintId)) {
                c.setStatus(newStatus);
                c.setResolutionNotes(notes);
                return true;
            }
        }
        return false;
    }
}

class RoomService {
    private List<RoomRequest> roomRequests = new ArrayList<>();
    private int counter = 1;

    public RoomRequest createRequest(String studentId, String currentRoom, String targetRoom) {
        String id = "REQ" + (100 + counter++);
        RoomRequest req = new RoomRequest(id, studentId, currentRoom, targetRoom);
        roomRequests.add(req);
        return req;
    }

    public List<RoomRequest> getAllRequests() {
        return roomRequests;
    }

    public boolean processRequest(String requestId, boolean approve) {
        for (RoomRequest req : roomRequests) {
            if (req.getRequestId().equalsIgnoreCase(requestId)) {
                req.setStatus(approve ? "APPROVED" : "REJECTED");
                return true;
            }
        }
        return false;
    }
}