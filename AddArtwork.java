package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddArtwork extends JFrame implements ActionListener {
    private JTextField titleField, mediumField, priceField, imageField;
    private JComboBox<String> artistComboBox; // For selecting the artist
    private JButton selectImageButton; // Button to select an image from the device
    private JButton backButton; // Button to go back to Home
    private String username; // Field to store the username

    public AddArtwork(String username) {
        this.username = username; // Store the username

        // Set up frame properties
        setTitle("Add Artwork");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make it full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 10, 10)); // Set grid layout with gaps

        // Create a gradient background panel
        GradientPanel gradientPanel = new GradientPanel();
        setContentPane(gradientPanel);
        gradientPanel.setLayout(new GridLayout(7, 2, 10, 10)); // Set grid layout on gradient panel

        // Create labels and text fields for artwork details
        createAndAddLabel("Title:", gradientPanel);
        titleField = createAndAddTextField(gradientPanel);

        createAndAddLabel("Medium:", gradientPanel);
        mediumField = createAndAddTextField(gradientPanel);

        createAndAddLabel("Price:", gradientPanel);
        priceField = createAndAddTextField(gradientPanel);

        createAndAddLabel("Image Path:", gradientPanel);
        imageField = createAndAddTextField(gradientPanel);

        // Button to select image from device
        selectImageButton = new JButton("Select Image");
        styleButton(selectImageButton);
        selectImageButton.addActionListener(e -> selectImage());
        gradientPanel.add(selectImageButton);

        createAndAddLabel("Artist:", gradientPanel);
        artistComboBox = new JComboBox<>(getArtists());
        artistComboBox.setPreferredSize(new Dimension(150, 30)); // Set preferred size for the combo box
        gradientPanel.add(artistComboBox);

        // Submit button
        JButton submitButton = new JButton("Add Artwork");
        styleButton(submitButton);
        submitButton.addActionListener(this);
        gradientPanel.add(submitButton);

        // Back button
        backButton = new JButton("Back");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 30)); // Make back button text bold and smaller
        backButton.addActionListener(e -> goBack());
        gradientPanel.add(backButton);

        setVisible(true);
    }

    private void createAndAddLabel(String text, JPanel panel) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Smaller font size and make it bold
        panel.add(label);
    }

    private JTextField createAndAddTextField(JPanel panel) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 20)); // Adjust font size for text fields
        textField.setPreferredSize(new Dimension(150, 30)); // Set preferred size for text fields
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        panel.add(textField);
        return textField;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 30)); // Make button text bold and smaller
        button.setFocusPainted(false); // Remove focus border
        button.setPreferredSize(new Dimension(120, 30)); // Set preferred size for buttons
    }

    private String[] getArtists() {
        try (Conn conn = new Conn()) {
            Connection connection = conn.getConnection();
            String query = "SELECT name FROM artists"; // Fetching names of all artists
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            java.util.List<String> artistList = new java.util.ArrayList<>();
            while (rs.next()) {
                artistList.add(rs.getString("name"));
            }
            return artistList.toArray(new String[0]); // Convert to array
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new String[] {}; // Return empty array on error
        }
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an Image");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToOpen = fileChooser.getSelectedFile();
            imageField.setText(fileToOpen.getAbsolutePath()); // Set the image path in the text field
        }
    }

    public void actionPerformed(ActionEvent e) {
        // Handle the button click event to insert artwork into the database
        String title = titleField.getText();
        String medium = mediumField.getText();
        String price = priceField.getText();
        String image = imageField.getText();
        String artistName = (String) artistComboBox.getSelectedItem();

        // Assuming you have a method to get artist ID by name
        int artistId = getArtistIdByName(artistName);

        if (artistId != -1) { // Check if artist ID is valid
            try (Conn conn = new Conn()) {
                Connection connection = conn.getConnection();
                String query = "INSERT INTO artwork (artist_id, title, medium, price, image) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, artistId);
                pstmt.setString(2, title);
                pstmt.setString(3, medium);
                pstmt.setDouble(4, Double.parseDouble(price));
                pstmt.setString(5, image);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Artwork added successfully!");

                // Refresh the ViewGallery to display the newly added artwork
                new ViewGallery(username).setVisible(true); // Pass username here
                this.setVisible(false); // Hide the current frame
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding artwork: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Artist not found.");
        }
    }

    private int getArtistIdByName(String name) {
        int artistId = -1; // Default value if not found
        try (Conn conn = new Conn()) {
            Connection connection = conn.getConnection();
            String query = "SELECT id FROM artists WHERE name = ?"; // Query to get artist ID
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                artistId = rs.getInt("id"); // Get artist ID from result
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return artistId; // Return the artist ID or -1 if not found
    }

    private void goBack() {
        new Home(username).setVisible(true); // Navigate back to Home
        this.setVisible(false); // Hide the current frame
    }

    public static void main(String[] args) {
        new AddArtwork("testUser"); // Pass a sample username
    }
}

// Custom JPanel to create a gradient background
class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, new Color(0, 102, 204), 0, getHeight(), new Color(0, 204, 255));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
