import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

/**
 * STUDENT ATTENDANCE MARKUP PAGE
 * Loads students into table -> mark Present/Absent -> Save to attendance table
 */
public class StudentAttedenceMarkup extends javax.swing.JFrame {
    private static final Logger logger = Logger.getLogger(StudentAttedenceMarkup.class.getName());

    // values passed from Dashboard/Login
    private final String subjectFromLogin;
    private final java.sql.Date dateFromLogin;

    // main constructor used from Dashboard
    public StudentAttedenceMarkup(String subjectFromLogin, java.sql.Date dateFromLogin) {
        this.subjectFromLogin = subjectFromLogin;
        this.dateFromLogin = dateFromLogin;
        initComponents();
        loadStudents();
        fillSubjectAndDateColumns();
    }

    // default constructor (for testing only)
    public StudentAttedenceMarkup() {
        this.subjectFromLogin = "";
        this.dateFromLogin = new java.sql.Date(System.currentTimeMillis());
        initComponents();
        loadStudents();
        fillSubjectAndDateColumns();
    }

    private void initComponents() {
        JLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Student Attendance Markup");

        JLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        JLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLabel1.setText("STUDENT ATTENDANCE MARKUP");

        // Table with Student, Roll, Present, Subject, Date
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Student Name", "Roll Number", "Present", "Subject", "Date"}
        ) {
            Class[] types = new Class[]{
                java.lang.String.class,  // name
                java.lang.String.class,  // roll
                java.lang.Boolean.class, // present checkbox
                java.lang.String.class,  // subject (read-only)
                java.lang.String.class   // date (read-only or editable if you change canEdit)
            };
            boolean[] canEdit = new boolean[]{false, false, true, false, false};

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jButton1.setText("SUBMIT");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addComponent(JLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 923, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(394, 394, 394))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(JLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

    // Load all students from database into table
    private void loadStudents() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT name, roll_number FROM students ORDER BY roll_number";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);
                while (rs.next()) {
                    String name = rs.getString("name");
                    String roll = rs.getString("roll_number");
                    // Present unchecked; Subject and Date will be filled after
                    model.addRow(new Object[]{name, roll, Boolean.FALSE, "", ""});
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading students", e);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading students: " + e.getMessage());
        }
    }

    // Fill subject + date columns for all rows using values from Login/Dashboard
    private void fillSubjectAndDateColumns() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        String subjectText = (subjectFromLogin == null) ? "" : subjectFromLogin.trim();
        String dateText = (dateFromLogin == null) ? "" : dateFromLogin.toString(); // yyyy-MM-dd

        for (int i = 0; i < model.getRowCount(); i++) {
            if (!subjectText.isEmpty()) {
                model.setValueAt(subjectText, i, 3); // Subject column index 3
            }
            if (!dateText.isEmpty()) {
                model.setValueAt(dateText, i, 4);    // Date column index 4
            }
        }
    }

    // SUBMIT button -> save attendance for all students
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        try (Connection con = DBConnection.getConnection()) {
            String findStudentSql = "SELECT id FROM students WHERE roll_number = ?";
            String findSubjectSql = "SELECT id FROM subjects WHERE name = ?";
            String insertSql = "INSERT INTO attendance (student_id, subject_id, attendance_date, status) " +
                               "VALUES (?, ?, ?, ?)";

            try (PreparedStatement psStudent = con.prepareStatement(findStudentSql);
                 PreparedStatement psSubject = con.prepareStatement(findSubjectSql);
                 PreparedStatement psInsert = con.prepareStatement(insertSql)) {

                int savedCount = 0;

                for (int i = 0; i < model.getRowCount(); i++) {
                    String roll = model.getValueAt(i, 1).toString();
                    Object attObj = model.getValueAt(i, 2);
                    Object subjObj = model.getValueAt(i, 3);
                    Object dateObj = model.getValueAt(i, 4);

                    if (subjObj == null || subjObj.toString().trim().isEmpty()) {
                        continue; // no subject -> cannot save
                    }
                    if (dateObj == null || dateObj.toString().trim().isEmpty()) {
                        continue; // no date -> cannot save
                    }

                    boolean present = attObj != null && (Boolean) attObj;
                    String status = present ? "P" : "A";
                    String subjectName = subjObj.toString().trim();
                    String dateStr = dateObj.toString().trim(); // must be yyyy-MM-dd

                    java.sql.Date attendanceDate = java.sql.Date.valueOf(dateStr);

                    // Find student ID
                    psStudent.setString(1, roll);
                    try (ResultSet rsStu = psStudent.executeQuery()) {
                        if (!rsStu.next()) continue;
                        int studentId = rsStu.getInt("id");

                        // Find subject ID
                        psSubject.setString(1, subjectName);
                        try (ResultSet rsSub = psSubject.executeQuery()) {
                            if (!rsSub.next()) continue;
                            int subjectId = rsSub.getInt("id");

                            // Insert attendance record
                            psInsert.setInt(1, studentId);
                            psInsert.setInt(2, subjectId);
                            psInsert.setDate(3, attendanceDate);
                            psInsert.setString(4, status);
                            psInsert.addBatch();
                            savedCount++;
                        }
                    }
                }

                if (savedCount > 0) {
                    psInsert.executeBatch();
                }

                javax.swing.JOptionPane.showMessageDialog(this,
                        " attendance records saved successfully!");
                loadStudents();
                fillSubjectAndDateColumns();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving attendance", e);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error saving attendance: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                    javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new StudentAttedenceMarkup().setVisible(true));
    }

    private javax.swing.JLabel JLabel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
}
