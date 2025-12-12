import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Simple JDBC helper class for connecting to MySQL.
 * Make sure MySQL is running and the database `attendance_db` exists.
 */
public class DBConnection {
    // Change these as per your MySQL setup
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/attendance_db";
    private static final String USER = "root";              // your MySQL username
    private static final String PASSWORD = "jayanth_csd_6731"; // your MySQL password

    static {
        try {
            // MySQL Connector/J 8+ driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a new connection to the database.
     * Always close it after use (try-with-resources is recommended).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
