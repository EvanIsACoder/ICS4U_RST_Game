//Main file to launch the game
package src;

import java.awt.CardLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

//Makes a custom j-frame
public class GameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    private MainMenu mainMenu;
    private KeybindsPanel keybindsPanel;
    private Clip backgroundMusicClip;

    public GameWindow() {
        this.setTitle("2-player fighting game");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Create the card layout manager
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create all screens
        mainMenu = new MainMenu(this);
        keybindsPanel = new KeybindsPanel(this);
        gamePanel = new GamePanel();
        
        // Add screens to the card panel
        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(keybindsPanel, "Keybinds");
        mainPanel.add(gamePanel, "GamePanel");
        
        // Start with the main menu
        cardLayout.show(mainPanel, "MainMenu");
        
        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo((Component)null);
        this.setVisible(true);

        // Play background music
        playBackgroundMusic("resources/Sounds/backgroundMusic.wav");
    }

    private void playBackgroundMusic(String filePath) {
        try {
            File musicFile = new File(filePath);
            if (musicFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                backgroundMusicClip = AudioSystem.getClip();
                backgroundMusicClip.open(audioStream);
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
            } else {
                System.err.println("Background music file not found: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
        }
        super.dispose();
    }

    // Method to switch to game panel
    public void showGamePanel() {
        cardLayout.show(mainPanel, "GamePanel");
        gamePanel.requestFocus();
    }

    // Method to switch to keybinds screen
    public void showKeybinds() {
        cardLayout.show(mainPanel, "Keybinds");
    }

    // Method to switch to main menu
    public void showMainMenu() {
        cardLayout.show(mainPanel, "MainMenu");
    }

    //Starts the game
    public static void main(String[] var0) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}