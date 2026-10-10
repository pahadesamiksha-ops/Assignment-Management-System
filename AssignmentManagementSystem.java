import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

// Class 1: Assignment
class Assignment {
    private String title;
    private LocalDate dueDate;
    private boolean isCompleted;

    public Assignment(String title, LocalDate dueDate) {
        this.title = title;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markCompleted() {
        this.isCompleted = true;
    }

    public void markPending() {
        this.isCompleted = false;
    }

    public String getStatus() {
        return isCompleted ? "Completed" : "Pending";
    }
}

// Class 2: Assignment Management System (Main)
public class AssignmentManagementSystem {
    private static ArrayList<Assignment> assignments = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n=== ASSIGNMENT MANAGEMENT SYSTEM ===");
            System.out.println("1. Add Assignment");
            System.out.println("2. View All Assignments");
            System.out.println("3. Update Assignment Status");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();  // clear buffer

            switch (choice) {
                case 1:
                    addAssignment();
                    break;
                case 2:
                    viewAssignments();
                    break;
                case 3:
                    updateStatus();
                    break;
                case 4:
                    System.out.println("Exiting the system... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 4);
    }

    private static void addAssignment() {
        System.out.print("Enter assignment title: ");
        String title = sc.nextLine();

        System.out.print("Enter due date (YYYY-MM-DD): ");
        String dateInput = sc.nextLine();

        try {
            LocalDate dueDate = LocalDate.parse(dateInput);
            assignments.add(new Assignment(title, dueDate));
            System.out.println("✅ Assignment added successfully!");
        } catch (Exception e) {
            System.out.println("❌ Invalid date format. Use YYYY-MM-DD.");
        }
    }

    private static void viewAssignments() {
        if (assignments.isEmpty()) {
            System.out.println("No assignments found.");
            return;
        }

        System.out.println("\n--- ASSIGNMENT LIST ---");
        System.out.printf("%-5s %-25s %-15s %-10s\n", "ID", "Title", "Due Date", "Status");
        for (int i = 0; i < assignments.size(); i++) {
            Assignment a = assignments.get(i);
            System.out.printf("%-5d %-25s %-15s %-10s\n", (i + 1), a.getTitle(), a.getDueDate(), a.getStatus());
        }
    }

    private static void updateStatus() {
        if (assignments.isEmpty()) {
            System.out.println("No assignments to update.");
            return;
        }

        viewAssignments();
        System.out.print("Enter assignment ID to update: ");
        int id = sc.nextInt();
        sc.nextLine(); // clear buffer

        if (id < 1 || id > assignments.size()) {
            System.out.println("❌ Invalid ID!");
            return;
        }

        Assignment a = assignments.get(id - 1);
        System.out.println("1. Mark as Completed");
        System.out.println("2. Mark as Pending");
        System.out.print("Enter your choice: ");
        int statusChoice = sc.nextInt();

        if (statusChoice == 1) {
            a.markCompleted();
            System.out.println("✅ Marked as Completed!");
        } else if (statusChoice == 2) {
            a.markPending();
            System.out.println("✅ Marked as Pending!");
        } else {
            System.out.println("❌ Invalid choice!");
        }
    }
}
