package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.border.*;
import java.awt.geom.RoundRectangle2D;

public class Signup extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private JButton signupButton, backButton;
    private Conn conn;

    public static void main(String[] args) {
        new Signup().setVisible(true);
    }

    public Signup() {
        // Initialize database connection
        conn = new Conn();

        // Frame properties
        setBounds(600, 250, 700, 406);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sign Up");

        // Set modern font globally
        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("SansSerif", Font.PLAIN, 14));

        // Custom panel with gradient background
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, Color.CYAN, 0, getHeight(), new Color(70, 130, 180));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Add rounded border to content pane
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true));

        // Username Label and TextField
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.DARK_GRAY);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setBounds(99, 86, 92, 26);
        contentPane.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(265, 91, 148, 20);
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(176, 224, 230), 2, true)); // Rounded border
        contentPane.add(usernameField);

        // Email Label and TextField
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.DARK_GRAY);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setBounds(99, 123, 92, 26);
        contentPane.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(265, 128, 148, 20);
        emailField.setBorder(BorderFactory.createLineBorder(new Color(176, 224, 230), 2, true)); // Rounded border
        contentPane.add(emailField);

        // Password Label and PasswordField
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.DARK_GRAY);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setBounds(99, 160, 92, 26);
        contentPane.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(265, 165, 148, 20);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(176, 224, 230), 2, true)); // Rounded border
        contentPane.add(passwordField);

        // SignUp Button
        signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Arial", Font.BOLD, 13));
        signupButton.setBounds(140, 289, 100, 30);
        signupButton.setBackground(new Color(46, 139, 87));
        signupButton.setForeground(Color.WHITE);
        signupButton.setBorder(BorderFactory.createLineBorder(new Color(46, 139, 87), 2, true));
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.addActionListener(this);
        contentPane.add(signupButton);

        // Back Button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setBounds(300, 289, 100, 30);
        backButton.setBackground(new Color(205, 92, 92));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(205, 92, 92), 2, true));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        contentPane.add(backButton);

        // Rounded border around main panel
        JPanel panel = new JPanel();
        panel.setForeground(new Color(34, 139, 34));
        panel.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 0), 2), "Create-Account",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(34, 139, 34)));
        panel.setBounds(31, 30, 640, 310);
        panel.setOpaque(false); // Transparent background to see gradient
        contentPane.add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton) {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            try (Connection dbConnection = conn.getConnection();
                 PreparedStatement stmt = dbConnection.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {

                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, password);  // You can hash the password here for better security

                int rows = stmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Sign up successful!");
                    new LoginScreen().setVisible(true);
                    this.dispose(); // Close Signup screen
                } else {
                    JOptionPane.showMessageDialog(this, "Sign up failed.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == backButton) {
            new LoginScreen().setVisible(true);
            this.dispose();
        }
    }
}
