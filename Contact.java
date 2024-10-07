package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

public class Contact extends JFrame implements ActionListener {

    private JButton githubButton;
    private JButton xButton;
    private JButton backButton;

    public Contact() {
        // Frame properties
        setTitle("Contact");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a panel for the contact information with a gradient background
        JPanel contactPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(70, 130, 180), 0, getHeight(), Color.CYAN);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contactPanel.setLayout(new GridLayout(3, 1, 10, 10));
        contactPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Phone Number Label
        JLabel phoneLabel = new JLabel("Phone Number: YOU CAN ADD YOUR NUMBER");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 16));
        phoneLabel.setForeground(Color.WHITE);
        contactPanel.add(phoneLabel);

        // Email Label
        JLabel emailLabel = new JLabel("Email: kb5234@gmail.com");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emailLabel.setForeground(Color.WHITE);
        contactPanel.add(emailLabel);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // GitHub Button
        githubButton = createModernButton("My GitHub");
        githubButton.setActionCommand("GITHUB");
        githubButton.addActionListener(this);
        buttonPanel.add(githubButton);

        // X (Twitter) Button
        xButton = createModernButton("My X Profile");
        xButton.setActionCommand("X_PROFILE");
        xButton.addActionListener(this);
        buttonPanel.add(xButton);

        // Back Button
        backButton = createModernButton("Back");
        backButton.addActionListener(e -> {
            new Home("User").setVisible(true); // Replace "User" with actual username if needed
            this.dispose(); // Close Contact screen
        });
        buttonPanel.add(backButton);

        // Add panels to the frame
        add(contactPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Create a modern button with rounded corners and hover effects
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

        // Add a mouse listener for hover effect
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

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if ("GITHUB".equals(e.getActionCommand())) {
                Desktop.getDesktop().browse(new URI("https://github.com/YOUR_USERNAME")); // Update with your URL
            } else if ("X_PROFILE".equals(e.getActionCommand())) {
                Desktop.getDesktop().browse(new URI("https://x.com/YOUR_USERNAME")); // Update with your URL or you can use your LinkedIn profile also
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Contact().setVisible(true);
    }
}
