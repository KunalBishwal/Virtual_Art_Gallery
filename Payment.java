package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Payment extends JDialog implements ActionListener {
    private String artworkTitle;
    private String username;
    private JButton payButton;
    private JButton backButton;

    public Payment(JFrame parentFrame, String artworkTitle, String username) {
        super(parentFrame, "Payment for " + artworkTitle, true); // true for modal dialog
        this.artworkTitle = artworkTitle;
        this.username = username;

        // Frame properties
        setSize(400, 250);
        setLocationRelativeTo(parentFrame); // Center on the parent frame
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Payment for " + artworkTitle, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        add(titleLabel, BorderLayout.NORTH);

        // Payment message
        JLabel messageLabel = new JLabel("Proceed to Payment", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(messageLabel, BorderLayout.CENTER);

        // Payment button
        payButton = new JButton("Proceed to Payment");
        payButton.setPreferredSize(new Dimension(200, 40));
        payButton.setFont(new Font("Arial", Font.BOLD, 14));
        payButton.setBackground(new Color(0, 102, 204));
        payButton.setForeground(Color.WHITE);
        payButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        payButton.addActionListener(this);
        
        // Back button
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            // Close the payment dialog without proceeding to payment
            dispose();
            // Return to Buy class
            new Buy(username).setVisible(true);
        });

        // Add buttons to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(payButton);
        buttonPanel.add(backButton); // Add back button
        add(buttonPanel, BorderLayout.SOUTH);

        // Final frame setup
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
       
        String paymentURL = "ADD YOUR URL FOR SCANNER";
        
        
        JOptionPane.showMessageDialog(this, "Redirecting to payment: " + paymentURL,
                "Payment Redirect", JOptionPane.INFORMATION_MESSAGE);

        // Open the payment URL in the default browser
        try {
            Desktop.getDesktop().browse(new java.net.URI(paymentURL));
            
            dispose(); 
           
            new Buy(username).setVisible(true); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame dummyFrame = new JFrame(); 
        new Payment(dummyFrame, "Sunset", "User"); 
    }
}
