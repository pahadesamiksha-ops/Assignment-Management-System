
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

class Assignment implements Serializable {
    private static final long serialVersionUID = 1L;

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void markCompleted() {
        isCompleted = true;
    }

    public void markPending() {
        isCompleted = false;
    }

    public String getStatus() {
        return isCompleted ? "Completed" : "Pending";
    }

    public boolean isOverdue() {
        return !isCompleted && dueDate.isBefore(LocalDate.now());
    }
}

public class AssignmentManagementSystem {

    private static final String FILE_NAME = "assignments.dat";
    private static final ArrayList<Assignment> assignments =
            new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadAssignments();

        int choice;
        do {
            System.out.println("\n================================");
            System.out.println("   ASSIGNMENT MANAGEMENT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Add Assignment");
            System.out.println("2. View All Assignments");
            System.out.println("3. Update Assignment Status");
            System.out.println("4. Edit Assignment");
            System.out.println("5. Delete Assignment");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

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
                    editAssignment();
                    break;
                case 5:
                    deleteAssignment();
                    break;
                case 6:
                    saveAssignments();
                    System.out.println("Goodbye! Your data is saved.");
                    break;
                default:
                    System.out.println(
                            "Invalid choice! Enter a number from 1 to 6.");
            }
        } while (choice != 6);

        sc.close();
    }

    // Read integers safely without crashing on text input
    private static int readInt() {
        while (true) {
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid whole number: ");
            }
        }
    }

    private static String readTitle(String prompt) {
        while (true) {
            System.out.print(prompt);
            String title = sc.nextLine().trim();

            if (!title.isEmpty()) {
                return title;
            }

            System.out.println("Title cannot be empty.");
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Invalid date! Use YYYY-MM-DD, e.g. 2026-12-25.");
            }
        }
    }

    private static void addAssignment() {
        String title = readTitle("Enter assignment title: ");
        LocalDate date = readDate("Enter due date (YYYY-MM-DD): ");

        assignments.add(new Assignment(title, date));
        saveAssignments();

        System.out.println("Assignment added successfully!");
    }

    private static void viewAssignments() {
        if (assignments.isEmpty()) {
            System.out.println("No assignments found.");
            return;
        }

        System.out.println("\n--- ASSIGNMENT LIST ---");
        System.out.printf("%-5s %-25s %-15s %-12s %-10s%n",
                "ID", "Title", "Due Date", "Status", "Deadline");

        for (int i = 0; i < assignments.size(); i++) {
            Assignment a = assignments.get(i);

            String deadline = a.isOverdue() ? "Overdue" : "-";

            System.out.printf("%-5d %-25.25s %-15s %-12s %-10s%n",
                    i + 1, a.getTitle(), a.getDueDate(),
                    a.getStatus(), deadline);
        }
    }

    private static int selectAssignment() {
        if (assignments.isEmpty()) {
            System.out.println("No assignments available.");
            return -1;
        }

        viewAssignments();
        System.out.print("Enter assignment ID: ");
        int id = readInt();

        if (id < 1 || id > assignments.size()) {
            System.out.println("Invalid assignment ID.");
            return -1;
        }

        return id - 1;
    }

    private static void updateStatus() {
        int index = selectAssignment();
        if (index == -1) {
            return;
        }

        Assignment a = assignments.get(index);

        System.out.println("1. Mark as Completed");
        System.out.println("2. Mark as Pending");
        System.out.print("Enter your choice: ");

        int choice = readInt();

        if (choice == 1) {
            a.markCompleted();
        } else if (choice == 2) {
            a.markPending();
        } else {
            System.out.println("Invalid status choice.");
            return;
        }

        saveAssignments();
        System.out.println("Assignment status updated successfully!");
    }

    private static void editAssignment() {
        int index = selectAssignment();
        if (index == -1) {
            return;
        }

        Assignment a = assignments.get(index);

        System.out.println("1. Edit Title");
        System.out.println("2. Edit Due Date");
        System.out.println("3. Edit Both");
        System.out.print("Enter your choice: ");

        int choice = readInt();

        switch (choice) {
            case 1:
                a.setTitle(readTitle("Enter new title: "));
                break;
            case 2:
                a.setDueDate(readDate("Enter new due date (YYYY-MM-DD): "));
                break;
            case 3:
                a.setTitle(readTitle("Enter new title: "));
                a.setDueDate(readDate("Enter new due date (YYYY-MM-DD): "));
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        saveAssignments();
        System.out.println("Assignment updated successfully!");
    }

    private static void deleteAssignment() {
        int index = selectAssignment();
        if (index == -1) {
            return;
        }

        System.out.print("Are you sure you want to delete this assignment? (y/n): ");
        String confirmation = sc.nextLine().trim();

        if (confirmation.equalsIgnoreCase("y")) {
            assignments.remove(index);
            saveAssignments();
            System.out.println("Assignment deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // Load saved assignments when the application starts
    @SuppressWarnings("unchecked")
    private static void loadAssignments() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {

            Object data = in.readObject();

            if (data instanceof ArrayList<?>) {
                ArrayList<?> loaded = (ArrayList<?>) data;

                for (Object item : loaded) {
                    if (item instanceof Assignment) {
                        assignments.add((Assignment) item);
                    }
                }
            }

            System.out.println("Saved assignments loaded successfully.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(
                    "Could not load saved data. Starting with an empty list.");
        }
    }

    // Save the current assignments to a local file
    private static void saveAssignments() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(assignments);

        } catch (IOException e) {
            System.out.println("Error saving assignments: " + e.getMessage());
        }
    }
}
