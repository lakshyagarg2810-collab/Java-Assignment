package com.kiet.inventory;

import java.sql.*;
import java.util.Scanner;

public class InventoryManagement {

    static Scanner sc = new Scanner(System.in);

    // (a) Insert at least three product records
    static void insertProducts(Connection con) {

        String sql = "INSERT INTO Product VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 1; i <= 3; i++) {

                System.out.println("\nEnter details for Product " + i);

                System.out.print("Product ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Product Name: ");
                String name = sc.nextLine();

                System.out.print("Category: ");
                String category = sc.nextLine();

                System.out.print("Quantity: ");
                int quantity = sc.nextInt();

                System.out.print("Price: ");
                double price = sc.nextDouble();

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, category);
                ps.setInt(4, quantity);
                ps.setDouble(5, price);

                try {
                    ps.executeUpdate();
                    System.out.println("Product inserted successfully!");
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Duplicate Product ID! Record not inserted.");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // (b) Retrieve products by category and quantity < 20
    static void retrieveProducts(Connection con) {

        String sql = "SELECT * FROM Product " +
                "WHERE category = ? AND quantity < 20 " +
                "ORDER BY price DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter category: ");
            sc.nextLine();
            String category = sc.nextLine();

            ps.setString(1, category);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("\nProduct ID: " + rs.getInt("product_id"));
                System.out.println("Name: " + rs.getString("product_name"));
                System.out.println("Category: " + rs.getString("category"));
                System.out.println("Quantity: " + rs.getInt("quantity"));
                System.out.println("Price: " + rs.getDouble("price"));
            }

            if (!found) {
                System.out.println("No matching products found.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // (c) Add quantity to existing stock
    static void updateStock(Connection con) {

        String sql = "UPDATE Product SET quantity = quantity + ? " +
                "WHERE product_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Product ID: ");
            int id = sc.nextInt();

            System.out.print("Enter quantity to add: ");
            int quantity = sc.nextInt();

            ps.setInt(1, quantity);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Stock updated successfully!");
            } else {
                System.out.println("Product does not exist.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // (d) Search product and update price
    static void updatePrice(Connection con) {

        try {
            System.out.print("Enter Product ID: ");
            int id = sc.nextInt();

            String search = "SELECT * FROM Product WHERE product_id = ?";

            try (PreparedStatement ps = con.prepareStatement(search)) {

                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    System.out.println("Product found!");
                    System.out.println("Name: " + rs.getString("product_name"));
                    System.out.println("Old Price: " + rs.getDouble("price"));

                    System.out.print("Enter new price: ");
                    double price = sc.nextDouble();

                    if (price > 0) {

                        String update = "UPDATE Product SET price = ? " +
                                "WHERE product_id = ?";

                        try (PreparedStatement ups = con.prepareStatement(update)) {

                            ups.setDouble(1, price);
                            ups.setInt(2, id);
                            ups.executeUpdate();
                        }

                        System.out.println("Updated Product Details:");
                        displayProduct(con, id);

                    } else {
                        System.out.println("Price must be greater than zero.");
                    }

                } else {
                    System.out.println("Product not found.");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Helper method to display product details
    static void displayProduct(Connection con, int id) throws SQLException {

        String sql = "SELECT * FROM Product WHERE product_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("ID: " + rs.getInt("product_id"));
                System.out.println("Name: " + rs.getString("product_name"));
                System.out.println("Category: " + rs.getString("category"));
                System.out.println("Quantity: " + rs.getInt("quantity"));
                System.out.println("Price: " + rs.getDouble("price"));
            }
        }
    }

    // (e) Display total inventory value
    static void totalInventoryValue(Connection con) {

        String sql = "SELECT SUM(quantity * price) AS total FROM Product";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("Total Inventory Value: " +
                        rs.getDouble("total"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // (e) Delete product only if quantity = 0
    static void deleteProduct(Connection con) {

        String sql = "DELETE FROM Product " +
                "WHERE product_id = ? AND quantity = 0";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.print("Enter Product ID to delete: ");
            int id = sc.nextInt();

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println(
                        "Product cannot be deleted. It may not exist or quantity is not zero."
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Main menu
    public static void main(String[] args) {

        try (Connection con = DBConnection.getConnection()) {

            System.out.println("Connected to MySQL successfully!");

            int choice;

            do {
                System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
                System.out.println("1. Insert Products");
                System.out.println("2. Retrieve Products by Category");
                System.out.println("3. Update Product Stock");
                System.out.println("4. Search and Update Product Price");
                System.out.println("5. Display Total Inventory Value");
                System.out.println("6. Delete Product");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        insertProducts(con);
                        break;

                    case 2:
                        retrieveProducts(con);
                        break;

                    case 3:
                        updateStock(con);
                        break;

                    case 4:
                        updatePrice(con);
                        break;

                    case 5:
                        totalInventoryValue(con);
                        break;

                    case 6:
                        deleteProduct(con);
                        break;

                    case 0:
                        System.out.println("Exiting application...");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } while (choice != 0);

        } catch (SQLException e) {
            System.out.println("Database Connection Error: " + e.getMessage());
        }
    }
}