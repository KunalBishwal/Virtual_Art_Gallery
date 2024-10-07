package project2;

import javax.swing.*;
import java.awt.*;

public class Loading extends JFrame {
    private JProgressBar progressBar;
    private JLabel loadingLabel;

    public Loading(String username) {
        setTitle("Loading...");
        setSize(800, 400); // Increased size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Custom panel to set a background image
        JPanel panel = new JPanel() {
            ImageIcon icon = null;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Load the image from resources
                try {
                    icon = new ImageIcon(getClass().getResource("/resources1/loading_background.jpg"));
                    if (icon.getImage() == null) {
                        throw new Exception("Image not found");
                    }
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    e.printStackTrace();
                    // If the image is not found, fill with a default color
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        // Progress bar settings
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Indeterminate mode for loading
        progressBar.setString("Loading...");
        progressBar.setStringPainted(true);

        loadingLabel = new JLabel("WELCOME BACK !!!!");
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Increased font size
        loadingLabel.setForeground(Color.WHITE);

        panel.setLayout(new BorderLayout());
        panel.add(loadingLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.SOUTH);
        setContentPane(panel);
        
        setVisible(true);

        // Simulate loading process for a fixed time and then switch to Home
        Timer timer = new Timer(3000, e -> {
            // After loading, transition to Home
            new Home(username).setVisible(true);
            this.dispose(); 
        });
        timer.setRepeats(false); 
        timer.start(); 
    }

    public static void main(String[] args) {
        new Loading("TestUser"); 
    }
}
