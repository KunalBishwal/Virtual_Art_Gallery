package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame implements ActionListener {
    String username;
    Conn conn; // Database connection
    JPopupMenu hamburgerMenu;

    public Home(String username) {
        this.username = username;


        conn = new Conn(); 

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());


        Image img = Toolkit.getDefaultToolkit().getImage("src/resources1/gallery_bg.png");
        if (img == null) {
            System.out.println("Image not found!"); 
            setBackground(Color.GRAY); 
        } else {
            ImageIcon background = new ImageIcon(img);
            JLabel bgLabel = new JLabel(background);
            bgLabel.setLayout(null); 

            JLabel header = new JLabel("Virtual Art Gallery for Emerging Artists");
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Serif", Font.BOLD, 50));
            header.setBounds(400, 50, 1200, 100);
            bgLabel.add(header);

            JButton viewGallery = createModernButton("View Gallery");
            viewGallery.setBounds(400, 200, 300, 50);
            bgLabel.add(viewGallery);

            JButton manageProfile = createModernButton("Manage Profile");
            manageProfile.setBounds(400, 300, 300, 50);
            bgLabel.add(manageProfile);

            JButton auction = createModernButton("Buy Pics");
            auction.setBounds(400, 400, 300, 50);
            bgLabel.add(auction);

            JButton reviews = createModernButton("Reviews");
            reviews.setBounds(400, 500, 300, 50);
            bgLabel.add(reviews);

            JButton aiRecommendations = createModernButton("AI");
            aiRecommendations.setBounds(400, 600, 300, 50);
            bgLabel.add(aiRecommendations);

            // Add an "Add Artwork" button
            JButton addArtwork = createModernButton("Add Artwork");
            addArtwork.setBounds(400, 700, 300, 50);
            bgLabel.add(addArtwork);

            // Add Hamburger Menu
            JButton hamburgerButton = createHamburgerButton();
            hamburgerButton.setBounds(20, 20, 50, 50);
            bgLabel.add(hamburgerButton);

      
            JPanel gifPanel = createGifPanel(); 
            gifPanel.setBounds(200, 800, 1200, 500); 
            bgLabel.add(gifPanel);

            JScrollPane scrollPane = new JScrollPane(bgLabel);
            scrollPane.setPreferredSize(new Dimension(1920, 1080)); 
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
            add(scrollPane, BorderLayout.CENTER); 
        }

        // Final frame setup
        setVisible(true);
    }

    // Create a panel to hold GIFs
    private JPanel createGifPanel() {
        JPanel gifPanel = new JPanel();
        gifPanel.setLayout(new GridLayout(3, 3)); // 2 rows, 3 columns for GIFs

        // Add GIFs to the panel
        for (int i = 1; i <= 9; i++) {
            String gifPath = "src/resources1/gif" + i + ".gif"; // Adjust path to your GIFs
            JLabel gifLabel = new JLabel(new ImageIcon(gifPath));
            gifLabel.setHorizontalAlignment(JLabel.CENTER); // Center align GIFs
            gifPanel.add(gifLabel);
        }

        return gifPanel;
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

        button.addActionListener(this); // Add action listener
        return button;
    }

    // Create the hamburger button
    private JButton createHamburgerButton() {
        JButton hamburgerButton = new JButton("☰"); // Hamburger icon
        hamburgerButton.setFont(new Font("Arial", Font.BOLD, 24));
        hamburgerButton.setBackground(new Color(0, 102, 204));
        hamburgerButton.setForeground(Color.WHITE);
        hamburgerButton.setFocusPainted(false);
        hamburgerButton.setBorderPainted(false);
        
        hamburgerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show the hamburger menu
                showHamburgerMenu(hamburgerButton);
            }
        });
        return hamburgerButton;
    }

    private void showHamburgerMenu(Component invoker) {
        if (hamburgerMenu == null) {
            hamburgerMenu = new JPopupMenu();

            // Add "Contact" menu item
            JMenuItem contactItem = new JMenuItem("Contact") {
                @Override
                public Dimension getPreferredSize() {
                    Dimension size = super.getPreferredSize();
                    size.width += 40; // Increase width
                    size.height += 20; // Increase height
                    return size;
                }
            };
            contactItem.setFont(new Font("Arial", Font.BOLD, 22)); // Increase font size
            contactItem.setBackground(new Color(70, 130, 180)); // Change color
            contactItem.setForeground(Color.WHITE); // Change text color
            contactItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new Contact().setVisible(true); // Open Contact screen
                    Home.this.setVisible(false);
                }
            });
            hamburgerMenu.add(contactItem);

            // Add "About" menu item
            JMenuItem aboutItem = new JMenuItem("About") {
                @Override
                public Dimension getPreferredSize() {
                    Dimension size = super.getPreferredSize();
                    size.width += 40; // Increase width
                    size.height += 20; // Increase height
                    return size;
                }
            };
            aboutItem.setFont(new Font("Arial", Font.BOLD, 22)); // Increase font size
            aboutItem.setBackground(new Color(70, 130, 180)); // Change color
            aboutItem.setForeground(Color.WHITE); // Change text color
            aboutItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new About(username).setVisible(true);
                    Home.this.setVisible(false);
                }
            });
            hamburgerMenu.add(aboutItem);

            // Add other menu items as needed
        }
        hamburgerMenu.show(invoker, invoker.getWidth(), 0); // Show menu
    }

    // Handle button actions
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        // Handle navigation between screens
        if (command.equals("View Gallery")) {
            new ViewGallery(username).setVisible(true);
            this.setVisible(false);
        } else if (command.equals("Manage Profile")) {
            new ManageProfile(username).setVisible(true);
            this.setVisible(false);
        } else if (command.equals("Buy Pics")) {
            new Buy(username).setVisible(true); // Navigate to Auction screen
            this.setVisible(false);
        } else if (command.equals("Reviews")) {
            new Reviews(username).setVisible(true);
            this.setVisible(false);
        } else if (command.equals("AI")) {
            AIRecommendations aiWindow = new AIRecommendations(username, this);
            aiWindow.setVisible(true);
            this.setVisible(false); // Hide the Home window but keep it active
        } else if (command.equals("Add Artwork")) {
            new AddArtwork(username).setVisible(true); // Navigate to Add Artwork screen
            this.setVisible(false);
        }
    }

    public static void main(String[] args) {
        new Home("SampleUser"); 
    }
}
