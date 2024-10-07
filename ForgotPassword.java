package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.border.EmptyBorder;

public class ForgotPassword extends JFrame implements ActionListener {
    private JPanel contentPane;
    private JTextField usernameField, emailField;
    private JButton searchButton, backButton;

    public static void main(String[] args) {
        new ForgotPassword().setVisible(true);
    }

    public ForgotPassword() {
        // Frame properties
        setBounds(600, 250, 700, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Forgot Password");

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

        // Create UI components
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.DARK_GRAY);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setBounds(100, 60, 150, 25);
        contentPane.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(300, 60, 150, 25);
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(176, 224, 230), 2, true)); // Rounded border
        contentPane.add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.DARK_GRAY);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setBounds(100, 100, 150, 25);
        contentPane.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(300, 100, 150, 25);
        emailField.setBorder(BorderFactory.createLineBorder(new Color(176, 224, 230), 2, true)); // Rounded border
        contentPane.add(emailField);

        // Search Button
        searchButton = new JButton("Retrieve Password");
        searchButton.setFont(new Font("Arial", Font.BOLD, 13));
        searchButton.setBounds(100, 150, 150, 30);
        searchButton.setBackground(new Color(46, 139, 87));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createLineBorder(new Color(46, 139, 87), 2, true));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(this);
        contentPane.add(searchButton);

        // Back Button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setBounds(300, 150, 100, 30);
        backButton.setBackground(new Color(205, 92, 92));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(205, 92, 92), 2, true));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        contentPane.add(backButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            retrievePassword();
        } else if (e.getSource() == backButton) {
            this.setVisible(false);
            new LoginScreen().setVisible(true); 
        }
    }

    private void retrievePassword() {
        String username = usernameField.getText();
        String email = emailField.getText();

        try (Conn con = new Conn();
             Connection dbConnection = con.getConnection()) {

            String sql = "SELECT password FROM users WHERE username=? AND email=?";
            try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String retrievedPassword = rs.getString("password");
                    showCustomMessageDialog("Your password is: " + retrievedPassword);
                } else {
                    showCustomMessageDialog("Username or Email not found.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showCustomMessageDialog("Error: " + ex.getMessage());
        }
    }

    // Custom message dialog
    private void showCustomMessageDialog(String message) {
        JDialog dialog = new JDialog(this, "Message", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        // Panel for message
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(255, 255, 255));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Bold text
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagePanel.add(messageLabel);

        // OK Button
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14)); // Bigger font
        okButton.setPreferredSize(new Dimension(120, 40)); // Bigger button size
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.setBackground(new Color(46, 139, 87));
        okButton.setForeground(Color.WHITE);
        okButton.setBorder(BorderFactory.createLineBorder(new Color(46, 139, 87), 2, true));
        okButton.addActionListener(e -> dialog.dispose());
        messagePanel.add(okButton);

        dialog.add(messagePanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
