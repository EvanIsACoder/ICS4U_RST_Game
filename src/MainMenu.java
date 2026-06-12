// Main Menu Screen. Displays title, player images and buttons for starting the game and instructions/keybinds.

package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MainMenu extends JPanel {
    private BufferedImage player1Image;
    private BufferedImage player2Image;
    private JButton playButton;
    private JButton keybindsButton;
    private GameWindow gameWindow;

    public MainMenu(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        this.setBackground(Color.BLACK);
        this.setLayout(null); // Use absolute positioning

        // Load player images on the side for visuals. 
        player1Image = loadImage("resources/images/Player_1_Xiao/xiaoBaseRight(1).png");
        player2Image = loadImage("resources/images/Player_2_Roland/rolandBaseRight.png");

        // Create Play Button
        playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial", Font.BOLD, 40));
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(new Color(50, 50, 50));
        playButton.setFocusPainted(false);
        playButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.showGamePanel();
            }
        });

        this.add(playButton);

        // Create Keybinds Button
        keybindsButton = new JButton("KEYBINDS / INSTRUCTIONS");
        keybindsButton.setFont(new Font("Arial", Font.BOLD, 20));
        keybindsButton.setForeground(Color.WHITE);
        keybindsButton.setBackground(new Color(50, 50, 50));
        keybindsButton.setFocusPainted(false);
        keybindsButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        keybindsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.showKeybinds();
            }
        });

        this.add(keybindsButton);
    }

    //helps with loading the images
    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            System.out.println("Could not load image: " + path);
            return null;
        }
    }

    //Draws the panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Draw "Title" text
        g2.setFont(new Font("Arial", Font.BOLD, 100));
        g2.setColor(Color.WHITE);
        String titleText = "Super Brawl";
        int titleX = (w - g2.getFontMetrics().stringWidth(titleText)) / 2;
        g2.drawString(titleText, titleX, h / 2 - 100);

        // Draw Play Button
        playButton.setBounds(w / 2 - 150, h / 2 - 40, 300, 80);

        // Draw Keybinds Button
        keybindsButton.setBounds(w / 2 - 200, h / 2 + 70, 400, 50);

        // Draw Player 1 image on the left
        if (player1Image != null) {
            int imageSize = 480;
            int p1ImageX = w / 4 - imageSize / 2;
            g.drawImage(player1Image, p1ImageX, h / 2 - imageSize / 2, imageSize, imageSize, null);
            g2.setFont(new Font("Arial", Font.BOLD, 32));
            g2.setColor(Color.WHITE);
            String p1Label = "Player 1";
            g2.drawString(p1Label, p1ImageX + (imageSize - g2.getFontMetrics().stringWidth(p1Label)) / 2, h / 2 + imageSize / 2 + 50);
        }

        // Draw Player 2 image on the right
        if (player2Image != null) {
            int imageSize = 480;
            int p2ImageX = 3 * w / 4 - imageSize / 2;
            g.drawImage(player2Image, p2ImageX, h / 2 - imageSize / 2, imageSize, imageSize, null);
            g2.setFont(new Font("Arial", Font.BOLD, 32));
            g2.setColor(Color.WHITE);
            String p2Label = "Player 2";
            int p2LabelX = p2ImageX + (imageSize - g2.getFontMetrics().stringWidth(p2Label)) / 2;
            g2.drawString(p2Label, p2LabelX, h / 2 + imageSize / 2 + 50);
        }
    }

    //Lets the panel interact with the user
    @Override
    public void addNotify() {
        super.addNotify();
        setFocusable(true);
    }
}
