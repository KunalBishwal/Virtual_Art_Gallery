package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Reviews extends JFrame {
    String username;
    JComboBox<String> artworkDropdown;  
    JTextArea reviewTextArea;
    JTextField userNameField;

    public Reviews(String username) {
        this.username = username;
        setTitle("Artist Reviews");
        setSize(1200, 800); // Set the size of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
        setLocationRelativeTo(null); // Center the window on screen

        // Create a custom JPanel for background
        GradientBackgroundPanel backgroundPanel = new GradientBackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components

        // Reviews header
        JLabel header = new JLabel("See what others are saying!");
        header.setFont(new Font("Arial", Font.BOLD, 36)); // Modern font
        header.setForeground(Color.BLACK); // Dark color for text
        header.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        backgroundPanel.add(header, gbc);

        // Scrollable reviews panel
        JPanel reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(reviewPanel);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Reviews")); // Title for scroll pane
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        backgroundPanel.add(scrollPane, gbc);

        // Fetch and display reviews from the database
        loadReviews(reviewPanel);

        // Dropdown for selecting artwork
        JLabel artworkLabelPrompt = new JLabel("Select Artwork:");
        artworkLabelPrompt.setForeground(Color.BLACK); // Dark color for text
        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(artworkLabelPrompt, gbc);

        artworkDropdown = new JComboBox<>();
        gbc.gridx = 1;
        backgroundPanel.add(artworkDropdown, gbc);
        loadArtworks(); // Load artworks into the dropdown

        // Input field for user name
        JLabel userNameLabel = new JLabel("Your Name:");
        userNameLabel.setForeground(Color.BLACK); // Dark color for text
        gbc.gridx = 0;
        gbc.gridy = 3;
        backgroundPanel.add(userNameLabel, gbc);

        userNameField = new JTextField();
        userNameField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        backgroundPanel.add(userNameField, gbc);

        // Input field for adding a new review
        JLabel reviewLabel = new JLabel("Your Review:");
        reviewLabel.setForeground(Color.BLACK); // Dark color for text
        gbc.gridx = 0;
        gbc.gridy = 4;
        backgroundPanel.add(reviewLabel, gbc);

        reviewTextArea = new JTextArea(5, 20);
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setWrapStyleWord(true);
        reviewTextArea.setFont(new Font("Arial", Font.PLAIN, 16)); // Modern font
        JScrollPane reviewScrollPane = new JScrollPane(reviewTextArea);
        reviewScrollPane.setPreferredSize(new Dimension(400, 100));
        gbc.gridx = 1;
        backgroundPanel.add(reviewScrollPane, gbc);

        // Submit button to add a new review
        JButton submitButton = new JButton("Submit Review");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16)); // Modern font
        submitButton.setBackground(new Color(52, 152, 219)); // Modern button color
        submitButton.setForeground(Color.WHITE);
        submitButton.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        backgroundPanel.add(submitButton, gbc);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                addReview();
                reviewPanel.removeAll(); // Clear previous reviews
                loadReviews(reviewPanel); // Reload reviews
                reviewPanel.revalidate();
                reviewPanel.repaint();
            }
        });

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16)); // Modern font
        backButton.setBackground(new Color(231, 76, 60)); // Modern button color
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        gbc.gridx = 0;
        gbc.gridy = 6;
        backgroundPanel.add(backButton, gbc);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new Home(username).setVisible(true);
                dispose(); // Close the current window
            }
        });

        // Panel for displaying artworks on the right side
        JPanel artworkPanel = new JPanel();
        artworkPanel.setLayout(new GridLayout(0, 1)); // Vertical layout for artworks
        JScrollPane artworkScrollPane = new JScrollPane(artworkPanel);
        artworkScrollPane.setPreferredSize(new Dimension(300, 300)); // Adjust size and position as needed
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 6; // Extend this component vertically
        backgroundPanel.add(artworkScrollPane, gbc);

        // Load artworks into the artwork panel
        loadArtworksIntoPanel(artworkPanel);
    }

    private void loadReviews(JPanel reviewPanel) {
        try (Conn c = new Conn(); 
             Connection conn = c.getConnection();
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM reviews";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String userName = rs.getString("user_name");
                String artworkName = rs.getString("artist_name");
                String reviewText = rs.getString("review_text");
                JLabel reviewLabel = new JLabel("<html><body style='width:600px;'><b>Name:</b> " + userName + " | <b>Artwork:</b> " + artworkName + " | <b>Review:</b> " + reviewText + "</body></html>");
                reviewLabel.setForeground(Color.BLACK); // Dark color for text
                reviewLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Bolder font
                reviewPanel.add(reviewLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching reviews: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadArtworks() {
        try (Conn c = new Conn();
             Connection conn = c.getConnection();
             Statement stmt = conn.createStatement()) {

            String query = "SELECT title FROM artwork";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String artworkTitle = rs.getString("title");
                artworkDropdown.addItem(artworkTitle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching artworks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadArtworksIntoPanel(JPanel artworkPanel) {
        try (Conn c = new Conn();
             Connection conn = c.getConnection();
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM artwork";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String title = rs.getString("title");
                String imagePath = rs.getString("image"); 

                // Load and display the artwork image
                ImageIcon artworkImage = new ImageIcon(imagePath);
                JLabel imageLabel = new JLabel(artworkImage);
                imageLabel.setPreferredSize(new Dimension(200, 200)); // Set a preferred size for the images
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                artworkPanel.add(imageLabel);
                
                JLabel artworkTitleLabel = new JLabel(title);
                artworkTitleLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Bolder font for title
                artworkTitleLabel.setForeground(Color.BLACK); // Dark color for text
                artworkPanel.add(artworkTitleLabel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching artworks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addReview() {
        String userName = userNameField.getText();
        String artworkTitle = (String) artworkDropdown.getSelectedItem();
        String reviewText = reviewTextArea.getText();

        // Validate inputs
        if (userName.isEmpty() || artworkTitle == null || reviewText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Conn c = new Conn();
             Connection conn = c.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO reviews (user_name, artist_name, review_text) VALUES (?, ?, ?)")) {

            pstmt.setString(1, userName);
            pstmt.setString(2, artworkTitle);
            pstmt.setString(3, reviewText);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Review submitted successfully!");

            userNameField.setText("");
            reviewTextArea.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting review: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Background Panel class to create a gradient background
    class GradientBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 204), 0, getHeight(), new Color(204, 255, 204));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Reviews("User").setVisible(true)); // Example username
    }
}
