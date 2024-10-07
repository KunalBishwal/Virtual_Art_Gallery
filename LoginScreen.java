package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginScreen extends JFrame implements ActionListener {
    private JPanel panel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, forgotPasswordButton;

    public LoginScreen() {
        setTitle("Login");
        setBounds(450, 200, 750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set custom background color (gradient)
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                // Gradient from light blue to dark blue
                Color color1 = new Color(176, 224, 230);
                Color color2 = new Color(70, 130, 180);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        panel.setLayout(null);
        setContentPane(panel);

        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(150, 100, 100, 30);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(Color.WHITE);
        panel.add(usernameLabel);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(150, 150, 100, 30);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel);

        // Text field for username
        usernameField = new JTextField();
        usernameField.setBounds(260, 100, 180, 30);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(176, 224, 230), 2, true));
        panel.add(usernameField);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setBounds(260, 150, 180, 30);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(176, 224, 230), 2, true));
        panel.add(passwordField);

        // Login button with rounded edges
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(46, 139, 87));
        loginButton.setBounds(200, 220, 120, 40);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(46, 139, 87), 2, true));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        panel.add(loginButton);

        // SignUp button with rounded edges
        signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Arial", Font.BOLD, 14));
        signupButton.setForeground(Color.WHITE);
        signupButton.setBackground(new Color(139, 69, 19));
        signupButton.setBounds(340, 220, 120, 40);
        signupButton.setFocusPainted(false);
        signupButton.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2, true));
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.addActionListener(this);
        panel.add(signupButton);

        // Forgot Password button with rounded edges
        forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.setFont(new Font("Arial", Font.BOLD, 12));
        forgotPasswordButton.setForeground(Color.WHITE);
        forgotPasswordButton.setBackground(new Color(205, 92, 92));
        forgotPasswordButton.setBounds(250, 280, 180, 40);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setBorder(BorderFactory.createLineBorder(new Color(205, 92, 92), 2, true));
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.addActionListener(this);
        panel.add(forgotPasswordButton);

        // Adding image to the panel
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources1/login.jpg"));
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(480, 100, 150, 150);
        panel.add(imageLabel);

        // Optional: Set a sleek font for the entire application
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 14));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginButton) {
            try (Conn conn = new Conn()) {
                Connection connection = conn.getConnection();
                
                if (connection != null) {
                    String sql = "SELECT * FROM users WHERE username=? AND password=?";
                    PreparedStatement st = connection.prepareStatement(sql);

                    st.setString(1, usernameField.getText());
                    st.setString(2, String.valueOf(passwordField.getPassword()));

                    ResultSet rs = st.executeQuery();
                    if (rs.next()) {
                        // Show loading screen before transitioning to Home screen
                        new Loading(usernameField.getText()); 
                        this.setVisible(false); 
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Login or Password!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Database connection failed!");
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (ae.getSource() == signupButton) {
            setVisible(false);
            new Signup().setVisible(true);
        } else if (ae.getSource() == forgotPasswordButton) {
            setVisible(false);
            new ForgotPassword().setVisible(true);
        }
    }

    public static void main(String[] args) {
        new LoginScreen().setVisible(true);
    }
}
