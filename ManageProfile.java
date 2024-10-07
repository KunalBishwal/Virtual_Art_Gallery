package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.*;

public class ManageProfile extends JFrame {
    private String username;
    private String profilePicPath;

    public ManageProfile(String username) {
        this.username = username;
        setTitle("Manage Profile");
        setLayout(null);

        // Background gradient
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(85, 98, 112);
                Color color2 = new Color(78, 205, 196);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setBounds(0, 0, 1920, 1080);
        gradientPanel.setLayout(null);
        add(gradientPanel);

        // Fetch user details from the database
        String email = null;
        String currentProfilePic = null;
        try (Conn c = new Conn();
             Connection conn = c.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT email, profile_pic FROM users WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
                currentProfilePic = rs.getString("profile_pic");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Small box for name and email
        JPanel userInfoBox = new JPanel();
        userInfoBox.setBackground(new Color(255, 255, 255, 150));
        userInfoBox.setBounds(600, 50, 400, 150);
        userInfoBox.setLayout(new GridLayout(2, 1)); // 2 rows, 1 column
        gradientPanel.add(userInfoBox);

        // Center the text in the labels
        JLabel nameLabel = new JLabel("Username: " + username, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userInfoBox.add(nameLabel);

        JLabel emailLabel = new JLabel("Email: " + email, SwingConstants.CENTER);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userInfoBox.add(emailLabel);

        // Display profile picture
        JLabel profilePicLabel = new JLabel();
        if (currentProfilePic != null) {
            ImageIcon profileIcon = new ImageIcon(currentProfilePic);
            Image profileImage = profileIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            profilePicLabel.setIcon(new ImageIcon(profileImage));
        }
        profilePicLabel.setBounds(700, 250, 200, 200);
        gradientPanel.add(profilePicLabel);

        // File chooser and Save Changes button
        JButton choosePicButton = createModernButton("Choose Profile Picture");
        choosePicButton.setBounds(650, 500, 300, 40);
        gradientPanel.add(choosePicButton);

        choosePicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, PNG & GIF Images", "jpg", "png", "gif");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    profilePicPath = selectedFile.getAbsolutePath();
                    ImageIcon newProfileIcon = new ImageIcon(profilePicPath);
                    Image newProfileImage = newProfileIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    profilePicLabel.setIcon(new ImageIcon(newProfileImage));
                }
            }
        });

        JButton saveChangesButton = createModernButton("Save Changes");
        saveChangesButton.setBounds(650, 550, 300, 40);
        gradientPanel.add(saveChangesButton);

        saveChangesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (profilePicPath != null) {
                    try (Conn c = new Conn();
                         Connection conn = c.getConnection();
                         PreparedStatement ps = conn.prepareStatement("UPDATE users SET profile_pic = ? WHERE username = ?")) {
                        ps.setString(1, profilePicPath);
                        ps.setString(2, username);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Profile picture updated successfully.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error updating profile picture.");
                    }
                }
            }
        });

        // Back button with modern style
        JButton backButton = createModernButton("Back");
        backButton.setBounds(50, 50, 100, 40);
        gradientPanel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Home(username).setVisible(true);
                setVisible(false);
            }
        });

        // Frame properties
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Create a modern button with custom style
    private JButton createModernButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // Draw rounded corners
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };

        button.setFocusPainted(false); 
        button.setBackground(new Color(0, 102, 204)); 
        button.setForeground(Color.WHITE); 
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBorderPainted(false); 
        button.setOpaque(false); 

        // Add hover effect for modern look
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 153, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204)); 
            }
        });

        return button;
    }

    public static void main(String[] args) {
        new ManageProfile("testUser").setVisible(true);
    }
}
