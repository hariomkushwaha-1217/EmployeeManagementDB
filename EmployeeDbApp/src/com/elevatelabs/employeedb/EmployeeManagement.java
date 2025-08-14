package com.elevatelabs.employeedb;

import java.sql.*;
import java.util.Scanner;

public class EmployeeManagement {

    static Connection con = null;
    static PreparedStatement pstmt = null;
    static ResultSet rs = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // 1. Load and Register the Driver class
            Class.forName("com.mysql.jdbc.Driver");

            // connection between java application and database server
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmployeeDB", "root", "admin");

            // 3. Create table if not exists inside EmployeeDB
            createTable(con);

            int choice;
            do {
                System.out.println("Employee Database Menu");
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter choice : ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> addEmployee(con, sc);
                    case 2 -> viewEmployees(con);
                    case 3 -> updateEmployee(con, sc);
                    case 4 -> deleteEmployee(con, sc);
                    case 5 -> System.out.println("Exiting...!");
                    default -> System.out.println("Invalid choice, try again.");
                }
            } 
            while (choice != 5);

        } 
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } 
        finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
                sc.close();
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Table creation
    private static void createTable(Connection con) throws SQLException {
        String createSQL = """
            create table if not exists employee (
                id int primary key,
                name varchar(100) not null,
                designation varchar(100) not null,
                salary double not null
            )
            """;
        try (Statement stmt = con.createStatement()) {
            stmt.execute(createSQL);
        }
    }

    //Add Employee
    private static void addEmployee(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Employee ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter Employee Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Employee Designation: ");
            String desg = sc.nextLine();
            System.out.print("Enter Employee Salary: ");
            double salary = sc.nextDouble();

            String sql = "insert into employee (id, name, designation, salary) values (?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, desg);
            pstmt.setDouble(4, salary);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " employee added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //View Employees
    private static void viewEmployees(Connection con) {
        try {
            String sql = "select * from employee";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            System.out.printf("%-5s %-20s %-20s %-10s%n", "ID", "Name", "Designation", "Salary");
            System.out.println("----------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-20s %-10.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("designation"),
                        rs.getDouble("salary"));
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Update Employee
    private static void updateEmployee(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Employee ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter New Name: ");
            String name = sc.nextLine();
            System.out.print("Enter New Designation: ");
            String desg = sc.nextLine();
            System.out.print("Enter New Salary: ");
            double salary = sc.nextDouble();
            
            String sql = "update employeedb.employee set name=?, designation=?, salary=? where id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, desg);
            pstmt.setDouble(3, salary);
            pstmt.setInt(4, id);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " employee updated successfully!");

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Delete Employee
    private static void deleteEmployee(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Employee ID to delete: ");
            int id = sc.nextInt();
            
            String sql = "delete from employeedb.employee where id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " employee deleted successfully!");

        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
