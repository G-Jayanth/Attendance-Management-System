import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

/**
 * ATTENDANCE RECORD DISPLAY PAGE
 * Shows complete attendance history for selected student with Present/Absent status
 */
public class AttedenceRecord2 extends javax.swing.JFrame {
    private static final Logger logger = Logger.getLogger(AttedenceRecord2.class.getName());
    private final int studentId;
    private final String studentName;

    /**
     * Default constructor (not used directly)
     */
    public AttedenceRecord2() {
        this.studentId = 0;
        this.studentName = "Unknown";
        initComponents();
    }

    /**
     * Main constructor - called from AttedenceRecord1 with student details
     */
    public AttedenceRecord2(int studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        initComponents();
        loadAttendance();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Attendance Records");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ATTENDANCE RECORDS");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Date", "Subject", "Status"}
        ) {
            boolean[] canEdit = new boolean[]{false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 16));
        jButton1.setText("BACK TO DASHBOARD");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(372, 372, 372)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 351, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    // Load attendance records for this student from database
    private void loadAttendance() {
        String title = String.format("Attendance Records for %s (ID: %d)", studentName, studentId);
        this.setTitle(title);
        jLabel1.setText("ATTENDANCE RECORDS - " + studentName);

        String sql = "SELECT a.attendance_date, s.name AS subject_name, a.status " +
                    "FROM attendance a " +
                    "JOIN subjects s ON a.subject_id = s.id " +
                    "WHERE a.student_id = ? " +
                    "ORDER BY a.attendance_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);
                int recordCount = 0;
                while (rs.next()) {
                    String date = rs.getString("attendance_date");
                    String subject = rs.getString("subject_name");
                    String status = rs.getString("status");
                    String statusDisplay = status.equals("P") ? "PRESENT" : "ABSENT";
                    model.addRow(new Object[]{date, subject, statusDisplay});
                    recordCount++;
                }
                if (recordCount == 0) {
                    model.addRow(new Object[]{"No records found", "", ""});
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading attendance records", e);
            javax.swing.JOptionPane.showMessageDialog(this, "Error loading records: " + e.getMessage());
        }
    }

    // BACK TO DASHBOARD button
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new Dashboard().setVisible(true);
        this.dispose();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new AttedenceRecord2().setVisible(true));
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
}
