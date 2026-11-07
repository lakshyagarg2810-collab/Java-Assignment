package com.kiet.hostel;

import java.sql.*;
import java.util.Scanner;

public class HostelManagement {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n===== KIET HOSTEL MANAGEMENT SYSTEM =====");
            System.out.println("1. Insert Hostel Student Records");
            System.out.println("2. Retrieve Students by Hostel");
            System.out.println("3. Update Hostel Fee");
            System.out.println("4. Search and Modify Room Number");
            System.out.println("5. Display Total Fee and Delete Student");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    insertStudents();
                    break;

                case 2:
                    retrieveStudents();
                    break;

                case 3:
                    updateFee();
                    break;

                case 4:
                    updateRoom();
                    break;

                case 5:
                    deleteStudent();
                    break;

                case 6:
                    System.out.println("Exiting application...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);

        sc.close();
    }

    // 1. INSERT AT LEAST THREE STUDENT RECORDS

    static void insertStudents() {

        String query = "INSERT INTO HostelStudent VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            int[][] records = {
                    {101, 0},
                    {102, 0},
                    {103, 0}
            };

            String[] names = {"Aman", "Riya", "Rahul"};
            String[] rolls = {"2301", "2302", "2303"};
            String[] hostels = {"Boys Hostel", "Girls Hostel", "Boys Hostel"};
            String[] rooms = {"101", "202", "103"};
            double[] fees = {60000, 55000, 45000};

            for (int i = 0; i < 3; i++) {

                try {
                    ps.setInt(1, records[i][0]);
                    ps.setString(2, names[i]);
                    ps.setString(3, rolls[i]);
                    ps.setString(4, hostels[i]);
                    ps.setString(5, rooms[i]);
                    ps.setDouble(6, fees[i]);

                    ps.executeUpdate();

                    System.out.println(names[i] + " inserted successfully!");

                } catch (SQLIntegrityConstraintViolationException e) {

                    System.out.println("Student ID " +
                            records[i][0] + " already exists!");

                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 2. RETRIEVE STUDENTS WITH FEE GREATER THAN 50000

    static void retrieveStudents() {

        System.out.print("Enter hostel name: ");
        String hostel = sc.nextLine();

        String query = "SELECT * FROM HostelStudent " +
                "WHERE hostel_name = ? AND fee > 50000 " +
                "ORDER BY fee DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, hostel);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("\nStudent ID: " +
                        rs.getInt("student_id"));

                System.out.println("Name: " +
                        rs.getString("student_name"));

                System.out.println("Roll Number: " +
                        rs.getString("roll_number"));

                System.out.println("Hostel: " +
                        rs.getString("hostel_name"));

                System.out.println("Room: " +
                        rs.getString("room_number"));

                System.out.println("Fee: " +
                        rs.getDouble("fee"));
            }

            if (!found) {
                System.out.println("No matching records found!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 3. UPDATE HOSTEL FEE

    static void updateFee() {

        System.out.print("Enter student ID: ");
        int id = sc.nextInt();

        System.out.print("Enter new hostel fee: ");
        double fee = sc.nextDouble();

        String query = "UPDATE HostelStudent SET fee = ? " +
                "WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setDouble(1, fee);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Hostel fee updated successfully!");
            } else {
                System.out.println("Student record does not exist!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 4. SEARCH STUDENT AND UPDATE ROOM NUMBER

    static void updateRoom() {

        System.out.print("Enter student ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        String select = "SELECT * FROM HostelStudent WHERE student_id = ?";

        String update = "UPDATE HostelStudent SET room_number = ? " +
                "WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(select)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\nStudent Found!");

                System.out.println("Name: " +
                        rs.getString("student_name"));

                System.out.println("Roll Number: " +
                        rs.getString("roll_number"));

                System.out.println("Hostel: " +
                        rs.getString("hostel_name"));

                System.out.println("Old Room: " +
                        rs.getString("room_number"));

                System.out.print("Enter new room number: ");
                String room = sc.nextLine();

                try (PreparedStatement ps2 =
                             con.prepareStatement(update)) {

                    ps2.setString(1, room);
                    ps2.setInt(2, id);

                    ps2.executeUpdate();
                }

                System.out.println("\nUpdated Student Details:");

                try (PreparedStatement ps3 =
                             con.prepareStatement(select)) {

                    ps3.setInt(1, id);

                    ResultSet updated = ps3.executeQuery();

                    if (updated.next()) {
                        System.out.println("Student ID: " +
                                updated.getInt("student_id"));

                        System.out.println("Name: " +
                                updated.getString("student_name"));

                        System.out.println("Hostel: " +
                                updated.getString("hostel_name"));

                        System.out.println("Room: " +
                                updated.getString("room_number"));

                        System.out.println("Fee: " +
                                updated.getDouble("fee"));
                    }
                }

            } else {
                System.out.println("Student not found!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // 5. DISPLAY TOTAL FEE AND DELETE STUDENT

    static void deleteStudent() {

        String sumQuery = "SELECT SUM(fee) AS total FROM HostelStudent";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sumQuery)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Total Hostel Fee Collected: " +
                        rs.getDouble("total"));
            }

            System.out.print("\nEnter student ID to delete: ");
            int id = sc.nextInt();

            String delete = "DELETE FROM HostelStudent " +
                    "WHERE student_id = ? AND fee > 0";

            try (PreparedStatement ps2 = con.prepareStatement(delete)) {

                ps2.setInt(1, id);

                int rows = ps2.executeUpdate();

                if (rows > 0) {
                    System.out.println("Student deleted successfully!");
                } else {
                    System.out.println(
                            "Record not found or fee is not fully paid!"
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}