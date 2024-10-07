package project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn implements AutoCloseable {
    private Connection connection;

    public Conn() {
        try {
            String url = "jdbc:mysql://localhost:3306/virtualartgallery";
            String username = "root";
            String password = "password";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
 
    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
