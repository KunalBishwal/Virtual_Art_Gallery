package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class AIRecommendations extends JFrame implements ActionListener {
    private String username;
    private JPanel recommendationPanel;
    private JTextArea userInput;
    private JButton sendButton, backButton;
    private Conn conn; // Database connection
    private JFrame homeFrame; // Reference to the Home frame

    public AIRecommendations(String username, JFrame homeFrame) {
        this.username = username;
        this.homeFrame = homeFrame; // Store reference to the Home frame
        conn = new Conn(); // Initialize database connection

        // Frame setup
        setTitle("AI Recommendations");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Allow closing without exiting
        setLocationRelativeTo(null);

        // Main panel with background image
        recommendationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = Toolkit.getDefaultToolkit().getImage("src/resources1/ai_recommendations_bg.jpg");
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        recommendationPanel.setLayout(new BoxLayout(recommendationPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(recommendationPanel), BorderLayout.CENTER);

        // User input area with a modern look
        userInput = new JTextArea(3, 40);
        userInput.setLineWrap(true);
        userInput.setWrapStyleWord(true);
        userInput.setFont(new Font("Arial", Font.PLAIN, 16));
        userInput.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        userInput.setMargin(new Insets(10, 10, 10, 10)); // Add padding to the text area

        sendButton = createModernButton("Chat");
        sendButton.addActionListener(this);

        // Back button setup
        backButton = createModernButton("Back");
        backButton.addActionListener(e -> {
            homeFrame.setVisible(true); // Show the Home screen
            this.dispose(); // Close AIRecommendations screen
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(new JScrollPane(userInput), BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(backButton, BorderLayout.WEST); // Add the Back button to the left side

        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

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

        button.setFocusPainted(false); // Remove focus highlight
        button.setBackground(new Color(0, 102, 204)); // Set background color
        button.setForeground(Color.WHITE); // Set text color
        button.setFont(new Font("Arial", Font.BOLD, 18)); // Set font
        button.setBorderPainted(false); // Remove border
        button.setOpaque(false); // Make button transparent

        // Add a mouse listener for hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 153, 255)); // Change color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204)); // Reset color
            }
        });

        return button;
    }

    public void actionPerformed(ActionEvent e) {
        String userMessage = userInput.getText().trim();
        if (!userMessage.isEmpty()) {
            addUserMessage(userMessage);
            userInput.setText("");
            handleAIResponse(userMessage);
        }
    }

    private void handleAIResponse(String userMessage) {
        showLoadingIndicator();

        // Here you would call your AI API
        String aiResponse = getAIResponse(userMessage); // Placeholder for AI API call

        removeLoadingIndicator();
        if (aiResponse != null) {
            addBotMessage(aiResponse);
        } else {
            showStyledMessageDialog("Error getting AI response.");
        }
    }

    private String getAIResponse(String userMessage) {
        // Mock AI response for demonstration
        return "This is a response to: " + userMessage;
    }

    private void addUserMessage(String message) {
        JLabel userMessageLabel = new JLabel("You: " + message);
        userMessageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userMessageLabel.setForeground(Color.WHITE); // Set text color
        recommendationPanel.add(userMessageLabel);
        recommendationPanel.revalidate();
        recommendationPanel.repaint();
    }

    private void addBotMessage(String message) {
        JLabel botMessageLabel = new JLabel("AI: " + message);
        botMessageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        botMessageLabel.setForeground(Color.WHITE); // Set text color
        recommendationPanel.add(botMessageLabel);
        recommendationPanel.revalidate();
        recommendationPanel.repaint();
    }

    private JLabel loadingLabel;

    private void showLoadingIndicator() {
        loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        loadingLabel.setForeground(Color.LIGHT_GRAY);
        loadingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        recommendationPanel.add(loadingLabel);
        recommendationPanel.revalidate();
        recommendationPanel.repaint();
    }

    private void removeLoadingIndicator() {
        if (loadingLabel != null) {
            recommendationPanel.remove(loadingLabel);
            loadingLabel = null;
            recommendationPanel.revalidate();
            recommendationPanel.repaint();
        }
    }

    private void showStyledMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AIRecommendations("testUser", new Home("testUser")));
    }
}
