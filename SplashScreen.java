package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SplashScreen extends JFrame {
    private static final int INITIAL_WIDTH = 300;
    private static final int INITIAL_HEIGHT = 225;
    private static final int FINAL_WIDTH = 800;
    private static final int FINAL_HEIGHT = 600;
    private static final int ANIMATION_DURATION = 2000; // Animation duration in milliseconds
    private static final int TIMER_DELAY = 15; // Delay between animation steps

    private JLabel imageLabel;
    private JLabel welcomeText;
    private Timer timer;
    private Timer pauseTimer;
    private BufferedImage splashImage;

    public SplashScreen() {
        // Frame properties
        setLayout(null);
        setUndecorated(true);
        setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
        setLocationRelativeTo(null);

        // Load the image using ImageIcon for synchronous loading
        ImageIcon imgIcon = new ImageIcon("src/resources1/splash.png"); // Ensure the path is correct
        if (imgIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
            System.out.println("Splash image not found! Setting default background color.");
            JLabel defaultLabel = new JLabel("Splash Image Not Found", SwingConstants.CENTER);
            defaultLabel.setFont(new Font("Arial", Font.BOLD, 30));
            add(defaultLabel, BorderLayout.CENTER);
            getContentPane().setBackground(Color.GRAY); // Set a default background color if the image is not found
        } else {
            // Create BufferedImage for smoother rendering
            splashImage = new BufferedImage(FINAL_WIDTH, FINAL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = splashImage.createGraphics();
            g2d.drawImage(imgIcon.getImage(), 0, 0, FINAL_WIDTH, FINAL_HEIGHT, null);
            g2d.dispose();

            // Image label for splash
            imageLabel = new JLabel(new ImageIcon(splashImage));
            imageLabel.setBounds(0, 0, INITIAL_WIDTH, INITIAL_HEIGHT);
            add(imageLabel);
        }

        // Add welcome text
        welcomeText = new JLabel("WELCOME TO THE APP", SwingConstants.CENTER);
        welcomeText.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeText.setForeground(Color.WHITE);
        welcomeText.setBounds(0, INITIAL_HEIGHT - 80, INITIAL_WIDTH, 50);
        add(welcomeText);

        // Set up the timer for animation
        timer = new Timer(TIMER_DELAY, new ActionListener() {
            private int step = 0;
            private final int totalSteps = ANIMATION_DURATION / TIMER_DELAY;

            @Override
            public void actionPerformed(ActionEvent e) {
                step++;
                if (step > totalSteps) {
                    timer.stop();
                    // Start the pause timer after animation ends
                    pauseTimer.start();
                } else {
                    double progress = (double) step / totalSteps;
                    int newWidth = (int) (INITIAL_WIDTH + progress * (FINAL_WIDTH - INITIAL_WIDTH));
                    int newHeight = (int) (INITIAL_HEIGHT + progress * (FINAL_HEIGHT - INITIAL_HEIGHT));

                    // Update size and position
                    setSize(newWidth, newHeight);
                    setLocationRelativeTo(null);

                    imageLabel.setBounds(0, 0, newWidth, newHeight);
                    welcomeText.setBounds(0, newHeight - 80, newWidth, 50);

                    // Update welcome text based on progress
                    welcomeText.setText("Loading... " + (int) (progress * 100) + "%");
                    welcomeText.repaint();
                    revalidate();
                }
            }
        });

        // Set up the timer for pause after the animation
        pauseTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseTimer.stop();
                setVisible(false);
                new LoginScreen().setVisible(true); // Transition to LoginScreen
            }
        });

        timer.start(); // Start the animation
    }

    public static void main(String[] args) {
        new SplashScreen().setVisible(true);
    }
}
