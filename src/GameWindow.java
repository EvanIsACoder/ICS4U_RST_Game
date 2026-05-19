package src;
import javax.swing.JFrame;

public class GameWindow extends JFrame {

    //Code for the game to open in a window
    public GameWindow() {
        setTitle("Controllable Box with Gravity");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        pack();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //Starts the game and lets it run
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new GameWindow());
    }
}
