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
   private final Box player1;
   private final Box player2;
   private final Attack attackP1;
   private final Attack attackP2;
   private final DrawingManager drawingManager;

   public int imageNum1;
   public int imageNum2;

   public int p1DashCD = 0;
   public int p2DashCD = 0;

   public GamePanel() {
        this.setPreferredSize(new Dimension(2550, 1350));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);dww
        this.addKeyListener(this);
        this.player1 = new Box();
        this.attackP1 = new Attack();
        player1.setX(150);
        this.player2 = new Box();
        this.attackP2 = new Attack();
        player2.setX(2150);
        player2.setSpeed(20);
        this.drawingManager = new DrawingManager();
        Timer var1 = new Timer(16, this);
        var1.start();
   }

   @Override
   protected void paintComponent(Graphics var1) {
        super.paintComponent(var1);
        var1.setColor(new Color(19, 19, 19));
        var1.fillRect(0, 1200, this.getWidth(), 200);
        this.drawingManager.drawPlayer1(var1, this.player1, imageNum1);
        this.drawingManager.drawPlayer2(var1, this.player2, imageNum2);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      p1DashCD--;
      p2DashCD--;
      this.player1.update(this.getWidth(), 1200);
      this.player2.update(this.getWidth(), 1200);

      if (player1.getXVelocity() == 0 && player1.getYVelocity() == 0)
      {
            imageNum1 = 1;
      }
      if (player2.getXVelocity() == 0 && player2.getYVelocity() == 0)
      {
            imageNum2 = 1;
      }

      if (imageNum1 == 1 || imageNum1 == 4)
      {
            player1.setHeight(460); 
            player1.setLength(500);  
      } else if (imageNum1 == 2 || imageNum1 == 3)
      {
            player1.setLength(500);
      } else 
      {
            player1.setLength(250);
            player1.setLength(250);
      }
      
      if (imageNum2 == 1)
      {
            player2.setLength(100);   
      } else 
      {
            player2.setLength(250);
      }

      this.repaint();
   }

   @Override
   public void keyPressed(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      
      //Player 1
      if (var2 == 65) {
            imageNum1 = 2; 
            this.player1.setLeftVelocity();
      }

      if (var2 == 68) {
            imageNum1 = 3; 
            this.player1.setRightVelocity();
      }

      if (var2 == 87) { 
            imageNum1 = 4; 
            this.player1.jump();
      }

      if (var2 == 83) {
            imageNum1 = 1; 
            this.player1.jumpCancel();
      }
      
      if (var2 == 82 && p1DashCD <= 0) {
            if (player1.getXVelocity() > 0) {
                player1.setX(player1.getX() + 500);
            } else if (player1.getXVelocity() < 0) {
                  player1.setX(player1.getX() - 500);
            } else if (player1.getYVelocity() < 0) {
                  player1.setY(player1.getY() - 400);
            }
            p1DashCD = 32; 
      }

      /*  testing to draw attack
      if (var2 == 84) {
            this.drawingManager.drawPlayer1(var1, this.player1, imageNum1);
      }*/

      //Player 2
      if (var2 == 37) { 
            imageNum2 = 2; 
            this.player2.setLeftVelocity();
      }

      if (var2 == 39) { 
            imageNum2 = 3;
            this.player2.setRightVelocity();
      }

      if (var2 == 38) {
            imageNum2 = 4; 
            this.player2.jump();
      }

      if (var2 == 40) {
            imageNum2 = 1; 
            this.player2.jumpCancel();
      }

      if (var2 == 80 && p2DashCD <= 0) {
            if (player2.getXVelocity() > 0) {
                player2.setX(player2.getX() + 300);
            } else if (player2.getXVelocity() < 0) {
                  player2.setX(player2.getX() - 300);
            } else if (player2.getYVelocity() < 0) {
                  player2.setY(player2.getY() - 300);
            }
            p2DashCD = 16; 
      }

   }

   @Override
   public void keyReleased(KeyEvent var1) 
   {
      int var2 = var1.getKeyCode();
      if (var2 == 65) {
            this.player1.stopLeftVelocity();
      }

      if (var2 == 68) {
            this.player1.stopRightVelocity();
      }

      if (var2 == 37) {
            this.player2.stopLeftVelocity();
      }

      if (var2 == 39) {
            this.player2.stopRightVelocity();
      }
    }

   @Override
   public void keyTyped(KeyEvent var1) 
   {
        //Empty but this needs to exist
   }
}
