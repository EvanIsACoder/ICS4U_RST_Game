package src;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameWindow extends JFrame {
   public GameWindow() {
        this.setTitle("Controllable Box with Gravity");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        GamePanel var1 = new GamePanel();
        this.add(var1);
        this.pack();
        this.setLocationRelativeTo((Component)null);
        this.setVisible(true);
   }

   public static void main(String[] var0) {
        SwingUtilities.invokeLater(() -> new GameWindow());
   }
}