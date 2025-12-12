public class TestDB {
    public static void main(String[] args) {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Try to connect
            java.sql.Connection con = java.sql.DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/attendance_db",
                "root",
                "jayanth_csd_6731"   // your real MySQL password
            );

            System.out.println("CONNECTED: " + con);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
