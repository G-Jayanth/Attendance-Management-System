import java.util.logging.Logger;

/**
 * DASHBOARD - Main navigation after class selection
 * Shows selected class details and two main buttons for attendance operations
 */
public class Dashboard extends javax.swing.JFrame {
    private static final Logger logger = Logger.getLogger(Dashboard.class.getName());

    // Values passed from Login
    private String currentSubject;
    private java.sql.Date currentDate;

    /**
     * Default constructor
     */
    public Dashboard() {
        initComponents();
    }

    /**
     * Old constructor (if you want to use it anywhere else)
     */
    public Dashboard(String classDetails) {
        initComponents();
        departmentDisplayLabel.setText("Selected: " + classDetails);
    }

    /**
     * New constructor used from Login:
     * receives class details, subject, and date
     */
    public Dashboard(String classDetails, String subject, java.sql.Date date) {
        initComponents();
        departmentDisplayLabel.setText("Selected: " + classDetails);
        this.currentSubject = subject;
        this.currentDate = date;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        departmentDisplayLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Attendance Management Dashboard");

        departmentDisplayLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); 
        departmentDisplayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        departmentDisplayLabel.setText("Department Name Goes Here");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); 
        jButton1.setText("STUDENT ATTENDANCE MARKUP");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); 
        jButton2.setText("ATTENDANCE RECORD");
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(262, 262, 262)
                .addComponent(departmentDisplayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 161, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(departmentDisplayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                .addContainerGap(146, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    // STUDENT ATTENDANCE MARKUP button -> open markup form
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Pass subject + date into StudentAttedenceMarkup
        new StudentAttedenceMarkup(currentSubject, currentDate).setVisible(true);
    }

    // ATTENDANCE RECORD button -> open record search form
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        new AttedenceRecord1().setVisible(true);
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
        java.awt.EventQueue.invokeLater(() -> new Dashboard().setVisible(true));
    }

    private javax.swing.JLabel departmentDisplayLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
}
