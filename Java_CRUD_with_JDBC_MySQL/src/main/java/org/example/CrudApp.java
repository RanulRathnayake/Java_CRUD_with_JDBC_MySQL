package org.example;

import java.sql.*;
import java.util.Scanner;

public class CrudApp {

    private static final String URL = "jdbc:mysql://localhost:3306/crud_demo";
    private static final String USER = "root";
    private static final String PASSWORD = "226226@Rgr";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✅ Connected to MySQL!");

            while (true) {
                System.out.println("\n=== CRUD Menu ===");
                System.out.println("1. Insert User");
                System.out.println("2. Read Users");
                System.out.println("3. Update User");
                System.out.println("4. Delete User");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> insertUser(conn, sc);
                    case 2 -> readUsers(conn);
                    case 3 -> updateUser(conn, sc);
                    case 4 -> deleteUser(conn, sc);
                    case 5 -> {
                        System.out.println("👋 Exiting...");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertUser(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();
            System.out.println("✅ User inserted!");
        }
    }

    private static void readUsers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Users ---");
            while (rs.next()) {
                System.out.printf("%d | %s | %s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
        }
    }

    private static void updateUser(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter user ID to update: ");
        int id = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        System.out.print("Enter new email: ");
        String email = sc.nextLine();

        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, id);
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println("✅ User updated!");
            else
                System.out.println("❌ User not found!");
        }

    }

    private static void deleteUser(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter user ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println("✅ User deleted!");
            else
                System.out.println("❌ User not found!");
        }
    }
}
