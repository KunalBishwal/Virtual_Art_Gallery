package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;

public class ViewGallery extends JFrame {
    String username;

    public ViewGallery(String username) {
        this.username = username;
        setTitle("Virtual Art Gallery");
        setLayout(new BorderLayout());

        // Load background image
        URL imgURL = getClass().getResource("/resources1/gallery_bg1.jpg");
        ImageIcon backgroundImage = null;

        if (imgURL == null) {
            System.out.println("Background image resource not found! Please check the path.");
            setBackground(Color.BLACK);
        } else {
            backgroundImage = new ImageIcon(imgURL);
        }

        // Create a panel for the background
        JLabel bgLabel = new JLabel();
        if (backgroundImage != null) {
            bgLabel.setIcon(backgroundImage);
            bgLabel.setLayout(new BorderLayout()); // Use BorderLayout for bgLabel
        } else {
            bgLabel.setBackground(Color.WHITE);
            bgLabel.setOpaque(true);
        }

        // Create scrollable panel to display artworks
        JPanel artPanel = new JPanel();
        artPanel.setLayout(new BoxLayout(artPanel, BoxLayout.Y_AXIS)); // Stack artworks vertically
        artPanel.setOpaque(false); // Transparent panel
        artPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        // Create a JScrollPane for the artPanel
        JScrollPane scrollPane = new JScrollPane(artPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border

        // Customizing scroll bar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(30, 0)); // Increase width
        verticalScrollBar.setUnitIncrement(20); // Increase scroll speed

        // Add the scrollPane to the background label
        bgLabel.add(scrollPane, BorderLayout.CENTER);
        add(bgLabel, BorderLayout.CENTER);

        // Fetch artworks from the database
        fetchArtworks(artPanel);

        // Title Label with underline and different font
JLabel titleLabel = new JLabel("GALLERY OF MASTERS");
titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 45)); // Set font to Comic Sans MS, bold, size 40
titleLabel.setForeground(Color.black); // Set text color
titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the title
bgLabel.add(titleLabel, BorderLayout.NORTH); // Add title at the top of bgLabel

// Add underline effect
titleLabel.setText("<html><u>" + titleLabel.getText() + "</u></html>"); // Underline the text



        // Modern Back button
        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(70, 130, 180));
        backButton.addActionListener(e -> {
            new Home(username).setVisible(true);
            setVisible(false);
        });

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make button panel transparent
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH); // Position at the bottom

        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleButton(JButton button, Color color) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(color); // Set button background color
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor
    }

    private void fetchArtworks(JPanel artPanel) {
        try (Conn c = new Conn();
             Connection conn = c.getConnection();
             Statement stmt = conn.createStatement()) {

            // Fetch artworks from the database
            String query = "SELECT * FROM artwork"; // Adjust this query if needed
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String title = rs.getString("title");
                String medium = rs.getString("medium");
                String price = rs.getString("price");
                String imagePath = rs.getString("image");

                // Load the image using the provided path and check if the path is valid
                ImageIcon artworkImage = new ImageIcon(imagePath);
                if (artworkImage.getIconWidth() == -1) {
                    System.out.println("Image not found: " + imagePath);
                    continue; // Skip this artwork if the image is invalid
                }

                // Resize the image to fit inside a specific box
                Image scaledImage = artworkImage.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Adjusted gap between artworks

                // Create a panel for artwork details
                JPanel artworkPanel = new JPanel();
                artworkPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align items to the left
                artworkPanel.setOpaque(false); // Transparent panel

                // Add details inside a small black box
                JPanel detailsPanel = new JPanel();
                detailsPanel.setBackground(new Color(0, 0, 0, 200)); // Semi-transparent black background
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Adjusted padding

                JLabel artLabel = new JLabel("<html><font color='white' size='5'><b>Title:</b> " + title + "<br><b>Medium:</b> " + medium + "<br><b>Price:</b> " + price + "</font></html>");
                artLabel.setForeground(Color.WHITE); // White color for text
                artLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Change font size
                artLabel.setPreferredSize(new Dimension(200, 100)); // Limit the label size

                detailsPanel.add(artLabel);
                artworkPanel.add(detailsPanel); // Add details on the left side
                artworkPanel.add(imageLabel); // Add image to the panel

                // Add hover effect for image preview
                imageLabel.addMouseListener(new MouseAdapter() {
                    JDialog previewDialog = null;

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // Create a new dialog for the medium-sized preview
                        previewDialog = new JDialog(ViewGallery.this, "Artwork Preview", true);
                        previewDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        previewDialog.setLayout(new BorderLayout());

                        // Scale the image to a larger size for preview
                        ImageIcon fullSizeImage = new ImageIcon(imagePath);
                        Image fullSizeScaledImage = fullSizeImage.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
                        JLabel previewLabel = new JLabel(new ImageIcon(fullSizeScaledImage));
                        previewDialog.add(previewLabel, BorderLayout.CENTER);

                        // Button to go full screen
                        JButton maximizeButton = new JButton("Full Screen");
                        styleButton(maximizeButton, new Color(34, 139, 34)); // Green color for full screen
                        maximizeButton.addActionListener(e1 -> {
                            JDialog fullScreenDialog = new JDialog(ViewGallery.this, "Full Screen", true);
                            fullScreenDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                            fullScreenDialog.setLayout(new BorderLayout());

                            JLabel fullScreenLabel = new JLabel(new ImageIcon(fullSizeImage.getImage().getScaledInstance(getToolkit().getScreenSize().width, getToolkit().getScreenSize().height, Image.SCALE_SMOOTH)));
                            fullScreenDialog.add(fullScreenLabel, BorderLayout.CENTER);

                            JButton closeButton = new JButton("Close");
                            styleButton(closeButton, new Color(220, 20, 60)); // Red color for close
                            closeButton.addActionListener(e2 -> fullScreenDialog.dispose());
                            fullScreenDialog.add(closeButton, BorderLayout.SOUTH);

                            fullScreenDialog.setUndecorated(true); // Remove title bar
                            fullScreenDialog.setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Set to screen size
                            fullScreenDialog.setLocationRelativeTo(null); // Center the dialog
                            fullScreenDialog.setVisible(true); // Show full-screen dialog
                        });

                        previewDialog.add(maximizeButton, BorderLayout.SOUTH);
                        previewDialog.pack();
                        previewDialog.setLocationRelativeTo(ViewGallery.this); // Center previewDialog
                        previewDialog.setVisible(true); // Show preview dialog
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (previewDialog != null) {
                            previewDialog.dispose(); // Close preview when mouse exits
                        }
                    }
                });

                artPanel.add(artworkPanel); // Add the artwork panel to the main art panel
            }

            // Revalidate and repaint after adding artworks
            artPanel.revalidate();
            artPanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ViewGallery("user1");
    }
}
