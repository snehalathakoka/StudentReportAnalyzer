package StudentReport;
import java.io.*;
import java.util.*;

// Serializable allows objects to be saved to a file
class Student implements Serializable {
    String name;
    int rollNo;
    int[] marks;
    int total;
    double average;
    String grade;

    public Student(String name, int rollNo, int subjects) {
        this.name = name;
        this.rollNo = rollNo;
        marks = new int[subjects];
    }

    public void calculate() {
        total = 0;
        for (int m : marks) total += m;
        average = (double) total / marks.length;

        if (average >= 90) grade = "A+";
        else if (average >= 80) grade = "A";
        else if (average >= 70) grade = "B";
        else if (average >= 60) grade = "C";
        else grade = "F";
    }

    public void display() {
        System.out.println("----------------------------");
        System.out.println("Name: " + name);
        System.out.println("Roll No: " + rollNo);
        System.out.print("Marks: ");
        for (int m : marks) System.out.print(m + " ");
        System.out.println("\nTotal: " + total);
        System.out.printf("Average: %.2f\n", average);
        System.out.println("Grade: " + grade);
    }
}

public class StudentReportAnalyzer {
    static final String FILE_NAME = "students.dat";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = loadData();

        while (true) {
            System.out.println("\n--- STUDENT REPORT ANALYZER ---");
            System.out.println("1. Add Student Records");
            System.out.println("2. View All Records");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addStudents(sc, students);
                    break;
                case 2:
                    viewStudents(students);
                    break;
                case 3:
                    saveData(students);
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    static void addStudents(Scanner sc, ArrayList<Student> students) {
        System.out.print("Enter number of students to add: ");
        int n = sc.nextInt();
        System.out.print("Enter number of subjects: ");
        int subjects = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for student " + (i + 1));
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Roll No: ");
            int rollNo = sc.nextInt();

            Student s = new Student(name, rollNo, subjects);
            for (int j = 0; j < subjects; j++) {
                System.out.print("Marks for subject " + (j + 1) + ": ");
                s.marks[j] = sc.nextInt();
            }
            sc.nextLine(); // consume newline

            s.calculate();
            students.add(s);
        }
        saveData(students);
    }

    static void viewStudents(ArrayList<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No records found.");
        } else {
            System.out.println("\n--- ALL STUDENT RECORDS ---");
            for (Student s : students) {
                s.display();
            }
        }
    }

    static void saveData(ArrayList<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    static ArrayList<Student> loadData() {
        ArrayList<Student> students = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (ArrayList<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found yet, ignore
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        return students;
    }
}
