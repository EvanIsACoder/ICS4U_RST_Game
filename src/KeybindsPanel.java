//Instructions panel for key bindings.
package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeybindsPanel extends JPanel {
    private JButton backButton;
    private GameWindow gameWindow;

    public KeybindsPanel(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        // Create Back Button
        backButton = new JButton("BACK TO MENU");
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(50, 50, 50));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.showMainMenu();
            }
        });
        this.add(backButton);
    }

    //Code to draw the panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Draw title
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        g2.setColor(Color.WHITE);
        String title = "KEYBINDS & INSTRUCTIONS";
        g2.drawString(title, (w - g2.getFontMetrics().stringWidth(title)) / 2, 110);

        // Back button - moved higher up
        backButton.setBounds(w / 2 - 100, h - 140, 200, 50);

        // Player 1 and Player 2 column headers centered in columns
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.setColor(new Color(231, 76, 60)); // Red for Player 1
        String p1Header = "PLAYER 1";
        int lineHeight = 40;
        int numKeybinds = 8;
        int totalKeybindHeight = numKeybinds * lineHeight;
        int startY = h / 2 - totalKeybindHeight - 80; // Position above centerline
        int headerY = startY - 40; // Just above the keybinds
        g2.drawString(p1Header, w / 4 - g2.getFontMetrics().stringWidth(p1Header) / 2, headerY);

        g2.setColor(new Color(135, 60, 180)); // Purple for Player 2
        String p2Header = "PLAYER 2";
        g2.drawString(p2Header, 3 * w / 4 - g2.getFontMetrics().stringWidth(p2Header) / 2, headerY);

        // Keybinds content
        g2.setFont(new Font("Arial", Font.PLAIN, 22));
        g2.setColor(Color.WHITE);

        int leftColumnX = w / 4;
        int rightColumnX = 3 * w / 4;

        // Player 1 Keybinds
        String[] p1Keybinds = {
            "A - Move Left",
            "D - Move Right",
            "W - Jump",
            "S - Cancel jump",
            "R - Dash",
            "T - Fireball Attack (25 dmg)",
            "G - Fire Spin Attack (75 dmg)",
            "V - Fire Slash Attack (75 dmg)"
        };

        // Player 2 Keybinds
        String[] p2Keybinds = {
            "Left Arrow - Move Left",
            "Right Arrow - Move Right",
            "Up Arrow - Jump",
            "Down Arrow - Cancel jump",
            "P - Dash",
            "O - Dash Attack (25 dmg)",
            "k - Slash Attack (50 dmg)",
            "l - Big Slash Attack (200 dmg)"
        };

        // Draw Player 1 keybinds - center aligned
        for (int i = 0; i < p1Keybinds.length; i++) {
            int textX = leftColumnX - g2.getFontMetrics().stringWidth(p1Keybinds[i]) / 2;
            g2.drawString(p1Keybinds[i], textX, startY + (i * lineHeight));
        }

        // Draw Player 2 keybinds - center aligned
        for (int i = 0; i < p2Keybinds.length; i++) {
            int textX = rightColumnX - g2.getFontMetrics().stringWidth(p2Keybinds[i]) / 2;
            g2.drawString(p2Keybinds[i], textX, startY + (i * lineHeight));
        }

        // Draw instructions at the bottom
        g2.setFont(new Font("Arial", Font.ITALIC, 18));
        g2.setColor(new Color(150, 150, 150));
        String instruction1 = "Both players share the same battlefield.";
        String instruction2 = "Reduce opponent HP to 0 to win!";
        g2.drawString(instruction1, (w - g2.getFontMetrics().stringWidth(instruction1)) / 2, h - 195);
        g2.drawString(instruction2, (w - g2.getFontMetrics().stringWidth(instruction2)) / 2, h - 165);

        // Draw combo system description
        g2.setFont(new Font("Arial", Font.ITALIC, 16));
        g2.setColor(new Color(255, 215, 0)); // Gold color for combo info
        String comboDesc = "COMBO SYSTEM: Land four consecutive hits, the next one does DOUBLE damage!";
        g2.drawString(comboDesc, (w - g2.getFontMetrics().stringWidth(comboDesc)) / 2, h - 225);
    }

    //Lets the panel interact with the user
    @Override
    public void addNotify() {
        super.addNotify();
        setFocusable(true);
    }
}
