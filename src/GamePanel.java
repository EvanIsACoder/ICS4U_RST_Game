package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
   //private static final int WIDTH = 800;
   //private static final int HEIGHT = 400;
   //private static final int GROUND_Y = 350;
   private final Box player1;
   private final Box player2;
   private final DrawingManager drawingManager;

   public GamePanel() {
      this.setPreferredSize(new Dimension(800, 400));
      this.setBackground(Color.BLACK);
      this.setFocusable(true);
      this.addKeyListener(this);
      this.player1 = new Box();
      this.player2 = new Box();
      this.drawingManager = new DrawingManager();
      Timer var1 = new Timer(16, this);
      var1.start();
   }

   protected void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      var1.setColor(new Color(19, 19, 19));
      var1.fillRect(0, 350, this.getWidth(), 50);
      this.drawingManager.drawPlayer1(var1, this.player1);
      this.drawingManager.drawPlayer2(var1, this.player2);
   }

   public void actionPerformed(ActionEvent var1) {
      this.player1.update(this.getWidth(), 350);
      this.player2.update(this.getWidth(), 350);
      this.repaint();
   }

   public void keyPressed(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 37) {
         this.player1.setLeftVelocity();
      }

      if (var2 == 39) {
         this.player1.setRightVelocity();
      }

      if (var2 == 38) {
         this.player1.jump();
      }

      if (var2 == 40) {
         this.player1.jumpCancel();
      }

      if (var2 == 65) {
         this.player2.setLeftVelocity();
      }

      if (var2 == 68) {
         this.player2.setRightVelocity();
      }

      if (var2 == 87) {
         this.player2.jump();
      }

      if (var2 == 83) {
         this.player2.jumpCancel();
      }

   }

   public void keyReleased(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 37) {
         this.player1.stopLeftVelocity();
      }

      if (var2 == 39) {
         this.player1.stopRightVelocity();
      }

      if (var2 == 65) {
         this.player2.stopLeftVelocity();
      }

      if (var2 == 68) {
         this.player2.stopRightVelocity();
      }

   }

   public void keyTyped(KeyEvent var1) {
   }
}
