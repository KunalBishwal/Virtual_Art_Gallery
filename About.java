package project2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class About extends JFrame implements ActionListener {

    JButton exitButton, backButton;
    JLabel titleLabel;
    TextArea infoTextArea;
    String projectInfo;
    String username; // Keep track of the username to pass it back to Home

    public About(String username) {
        this.username = username; // Initialize username
        // Set up the frame layout and properties
        setLayout(null);
        getContentPane().setBackground(new Color(60, 63, 65));  // Set background color

        // Title label
        titleLabel = new JLabel("About Virtual Art Gallery");
        titleLabel.setBounds(70, 10, 400, 50);
        titleLabel.setForeground(new Color(255, 69, 0)); // Set color for title
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));  // Font styling
        add(titleLabel);

        // Project Information
        projectInfo = "The Virtual Art Gallery Project aims to create an immersive and user-friendly platform "
                + "for emerging artists to showcase their artwork and connect with art enthusiasts.\n\n"
                + "Features of the Project:\n"
                + "• Artist profile and portfolio management\n"
                + "• Real-time auction system for artworks\n"
                + "• Social features for reviews and follows\n"
                + "• AI art recommendations to enhance user experience\n"
                + "• Secure payment options for buyers\n\n"
                + "Advantages of the Project:\n"
                + "• Facilitates easy navigation and discovery of art\n"
                + "• Supports artists in gaining visibility\n"
                + "• Provides a platform for art sales and transactions\n"
                + "• Reduces barriers for art appreciation and acquisition";

        // TextArea for displaying the project info
        infoTextArea = new TextArea(projectInfo, 10, 40, Scrollbar.VERTICAL);
        infoTextArea.setEditable(false);
        infoTextArea.setBounds(30, 80, 440, 300);
        infoTextArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoTextArea.setBackground(new Color(230, 230, 250));  // Light lavender background
        infoTextArea.setForeground(Color.BLACK);  // Text color
        add(infoTextArea);

        // Exit Button with rounded borders and styling
        exitButton = new JButton("Exit");
        exitButton.setBounds(280, 400, 120, 40);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(255, 69, 0));  // Orange button color
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setBorder(new RoundedBorder(20));  // Rounded borders
        exitButton.addActionListener(this);
        add(exitButton);

        // Back Button to return to Home
        backButton = new JButton("Back to Home");
        backButton.setBounds(100, 400, 150, 40);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(70, 130, 180));  // Blue button color
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBorder(new RoundedBorder(20));  // Rounded borders
        backButton.addActionListener(this);
        add(backButton);

        // Frame settings
        setBounds(600, 220, 500, 500);
        setTitle("About Virtual Art Gallery");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Rounded border class for the button
    class RoundedBorder implements javax.swing.border.Border {
        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.WHITE);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    // Close the window or navigate back when buttons are clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            dispose(); // Close window
        } else if (e.getSource() == backButton) {
            new Home(username).setVisible(true); // Navigate back to Home
            dispose(); // Close About window
        }
    }

    public static void main(String[] args) {
        new About("testUser"); // Test with example username
    }
}
