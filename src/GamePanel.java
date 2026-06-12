//This is the file that runs most of the game
package src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;  
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

// This class manages the main game panel, including drawing, player movement, attacks, and game state.
//it also implements the key listener and action listener for the game loop and player input.
public class GamePanel extends JPanel implements ActionListener, KeyListener {
   private final Box player1;
   private final Box player2;
   private final Attack attackP1;
   private final Attack attackP2;
   private final DrawingManager drawingManager;

   private Image bgWall, bgFloor, bgWall2; 
   private Image mainCamFrame, battleFrame, lowerFrame, topFrame;

   // Get the default screen toolkit
   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   public int panelWidth = screenSize.width;
   public int panelHeight = screenSize.height;

   //all the variables to manage what images are shown
   public int imageNum1;
   public int imageNum2;
   public int attackNum1;
   public int attackNum2;

   //variables to manage the cooldowns of the attacks and dashes, as well as the combo system
   public int p1DashCD = 0;
   public int autoAttackCancel1 = 0;
   public int p2DashCD = 0;
   public int autoAttackCancel2 = 0;

   public int fireballCD = 0;
   public int fireSpinCD = 0;
   public int fireSlashCD = 0;

   public int flashCD = 0;
   public int swingCD = 0;
   public int slashCD = 0;

   //variables to manage combo system
   public int comboCharge1 = 0;
   public int comboCharge2 = 0;
   private static final int MAX_COMBO_CHARGE = 4;

   //variables for the damage numbers that pop up when you hit the other player
   private java.util.List<ScatterDamage> damageNumbers = new java.util.ArrayList<>();
   private int p1HitCooldown = 0;
   private int p2HitCooldown = 0;


   //method that should allow for sounds to play. currently not working
   private void playSound(String soundFile) {
      try {
            File file = new File("resources/Sounds/" + soundFile);
            if (file.exists()) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
      } else {
            System.err.println("Sound file not found: " + soundFile);
      } } catch (Exception e) {
            e.printStackTrace();
        }
      }
   
//the actual game panel constructor, where the images are loaded, the player and attack objects are created, and the game loop timer is started.
      public GamePanel() {
            bgWall = loadImage("/images/Texture2D/Xiao_Background_Wall.png");
            bgFloor = loadImage("/images/Texture2D/Xiao_Background_Floor.png");
            bgWall2 = loadImage("/images/Texture2D/Xiao_Background_Wall2.png");
            
            mainCamFrame = loadImage("/images/Texture2D/MainCam_Frame.png");
            battleFrame = loadImage("/images/Texture2D/frame.png");
            lowerFrame = loadImage("/images/Texture2D/OverlayUI_LowerFrame.png");
            topFrame = loadImage("/images/Texture2D/backgroundBase.png");

            //sets up the game
            this.setPreferredSize(new Dimension(panelWidth, panelHeight));
            this.setBackground(Color.BLACK);
            this.setFocusable(true);
            this.addKeyListener(this);
            this.player1 = new Box();
            this.attackP1 = new Attack();
            player1.setX(150);
            this.player2 = new Box();
            this.attackP2 = new Attack();
            player2.setX(2150);
            player2.setSpeed(20);
            attackP2.setAttackX(player2.getX());
            this.drawingManager = new DrawingManager();
            
            Timer var1 = new Timer(16, this);
            var1.start();
      }

      //loads the background images
      private Image loadImage(String path) {
            try {
                  URL url = getClass().getResource(path);
                  if (url != null) return ImageIO.read(url);

                  File file = new File("resources" + path); 
                  if (file.exists()) {
                        return ImageIO.read(file);
                  }
            } catch (IOException e) {
                  e.printStackTrace();
            }

            return null;
      }

      //huge method that calls all the other drawing methods.
      @Override
      protected void paintComponent(Graphics var1) {
            super.paintComponent(var1);


            int screenW = getWidth();
            int screenH = getHeight();

            //draws the background in the correct order, then draws the players and attacks, then draws the UI elements on top of everything.
            // 1. Sky/background wall
            if (bgWall != null){
                  var1.drawImage(bgWall, 0, 0, screenW, screenH, this);
            }

            // 2. Floor
            if (bgFloor != null) {
                  double ratio = (double) screenW / bgFloor.getWidth(null);
                  int drawH = (int) (bgFloor.getHeight(null) * ratio);
                  int y = (int) (screenH * 0.004); 
                  var1.drawImage(bgFloor, 0, y, screenW, drawH, this);
                  
                  //Wall in front
                  if (bgWall2 != null) {
                        double wallRatio = (double) screenW / bgWall2.getWidth(null);
                        int wallDrawH = (int) (bgWall2.getHeight(null) * wallRatio);
                        int wallY = y + (int) (drawH * 0.72); 
                        var1.drawImage(bgWall2, 0, wallY, screenW, wallDrawH, this);
                  }
            }

            this.drawingManager.drawPlayer1(var1, this.player1, imageNum1);
            this.drawingManager.drawPlayer2(var1, this.player2, imageNum2);
            this.drawingManager.drawAttack1(var1, this.attackP1, attackNum1);
            this.drawingManager.drawAttack2(var1, this.attackP2, attackNum2);

            // 3. the game frame and spaces it nicely
            if (mainCamFrame != null) {
                  int paddingW = (int)(screenW * 0.01); 
                  int paddingH = (int)(screenH * 0.01);

                  int frameX = paddingW;
                  int frameY = paddingH;
                  int frameW = screenW - (paddingW * 2);
                  int frameH = screenH - (paddingH * 2);
                  
                  var1.drawImage(mainCamFrame, frameX, frameY, frameW, frameH, this);
            }
      
            //another layer of the frame
            if (battleFrame != null){
                  var1.drawImage(battleFrame, 0, 0, screenW, screenH, this);
            }
            
            //another layer of the frame
            if (lowerFrame != null) {
                  double lowerRatio = (double) screenW / lowerFrame.getWidth(null);
                  int lowerDrawH = (int) (lowerFrame.getHeight(null) * lowerRatio);
      
                  int lowerY = screenH - lowerDrawH;

                  var1.drawImage(lowerFrame, 0, lowerY+10, screenW, lowerDrawH, this);
            }

            //another frame layer
            if (topFrame != null) {
                  double topRatio = (double) screenW / topFrame.getWidth(null);
                  int topDrawH = (int) (topFrame.getHeight(null) * topRatio);
                  var1.drawImage(topFrame, 0, 0, screenW, topDrawH, this);
            }

            //Health bars
            drawHealthBar(var1, 50, 55, player1.getHp(), player1.getMaxHp(), new Color(231, 76, 60), "P1");
            drawHealthBar(var1, screenW - 400, 55, player2.getHp(), player2.getMaxHp(), new Color(135, 60, 180), "P2");

            //draw cooldown counters
            int cooldownRowY = 55;
            drawCooldownCounter(var1, 430, cooldownRowY, "Fireball");
            drawCooldownCounter(var1, 485, cooldownRowY, "Fire spin");
            drawCooldownCounter(var1, 540, cooldownRowY, "Fire slash");
            drawCooldownCounter(var1, 595, cooldownRowY, "DashP1");

            drawCooldownCounter(var1, screenW - 635, cooldownRowY, "Flash");
            drawCooldownCounter(var1, screenW - 580, cooldownRowY, "Swing");
            drawCooldownCounter(var1, screenW - 525, cooldownRowY, "Slash");
            drawCooldownCounter(var1, screenW - 470, cooldownRowY, "DashP2");

            //draw combo bar
            int comboSectionW = 52;
            int comboSpacing = 8;
            int comboTotalWidth = MAX_COMBO_CHARGE * comboSectionW + (MAX_COMBO_CHARGE - 1) * comboSpacing;
            drawComboBar(var1, 50, 100, comboCharge1, MAX_COMBO_CHARGE, new Color(255, 255, 255), "COMBO1");
            drawComboBar(var1, screenW - 50 - comboTotalWidth, 100, comboCharge2, MAX_COMBO_CHARGE, new Color(255, 255, 255), "COMBO2");

            //if the match has ended, draw the game over screen on top of everything
            if (matchEnded) {
                  drawGameOver(var1, screenW, screenH);
            }

            //draws the damage numbers on the screen when a hit is registered
            renderScatteredDamage(var1);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
            //checks game ending
            if (matchEnded) {
                  this.repaint(); 
                  return; 
            }
            
            verifyEndCondition();

            //manages the cooldowns for the attacks and dashes, also will cancel the animation so you can't just hold the attack forever
            p1DashCD--;
            fireballCD--;
            fireSpinCD--;
            fireSlashCD--;
            autoAttackCancel1--;

            p2DashCD--;
            flashCD--;
            swingCD--;
            slashCD--;
            autoAttackCancel2--;

            //updates the players to check they are in the playable area
            this.player1.update(this.getWidth(), panelHeight, panelHeight - 400);
            this.player2.update(this.getWidth(), panelHeight, panelHeight - 400);

            processRegulatedCombat();
      
            //makes the players face each other when they are idle
            if (player1.getXVelocity() == 0 && player1.getYVelocity() == 0 && attackNum1 == 0)
            {
                  if (player1.getX() > player2.getX()) {
                        imageNum1 = 0;
                  } else {
                        imageNum1 = 1;
                  }
            }

            if (player2.getXVelocity() == 0 && player2.getYVelocity() == 0 && attackNum2 == 0) {
                  if (player2.getX() > player1.getX()) {
                        imageNum2 = 0;
                  } else {
                        imageNum2 = 1;
                  }
            }

            //Play movement sound
            if (player1.getXVelocity() > 0 || player2.getXVelocity() > 0) {
                  playSound("movement.wav");
            }

            //depending on which attack is being used, it will set the correct image to be drawn
            if (attackNum1 == 1) {
                  imageNum1 = 5;
            } else if (attackNum1 == 2) {
                  imageNum1 = 6;
            } else if (attackNum1 == 3) {
                  imageNum1 = 7;
            } else if (attackNum1 == 4) {
                  imageNum1 = 8;
            } else if (attackNum1 == 5) {
                  imageNum1 = 9;
            } else if (attackNum1 == 6) {
                  imageNum1 = 10;
            }

            if (attackNum2 == 1) {
                  imageNum2 = 5;
            } else if (attackNum2 == 2) {
                  imageNum2 = 6;
            } else if (attackNum2 == 3) {
                  imageNum2 = 7;
            } else if (attackNum2 == 4) {
                  imageNum2 = 8;
            } else if (attackNum2 == 5) {
                  imageNum2 = 9;
            } else if (attackNum2 == 6) {
                  imageNum2 = 10;
            }

            //moves the fireball when being used
            if (fireballCD > 0 && attackNum1 == 1) {
                  playSound("fireSpear.wav");
                  attackP1.setAttackX(attackP1.getAttackX() - 30);
            } else if (fireballCD > 0 && attackNum1 == 2) {
                  attackP1.setAttackX(attackP1.getAttackX() + 30);
            }

            //cancels the attacks of players once the time to cancel is reached
            if (autoAttackCancel1 == 0) {
                  attackNum1 = 0;
            }

            if (autoAttackCancel2 == 0) {
                  attackNum2 = 0;
            }
      
            //repaints the screen to update the visuals
            this.repaint();
      }

      //manages all inputs
      @Override
      public void keyPressed(KeyEvent var1) {
            int var2 = var1.getKeyCode();
            
            if (matchEnded) {
                  if (var2 == KeyEvent.VK_ENTER) { 
                        hardResetCurrentMatch(); 
                  }

                  return;
            }

            //Player 1
            if (var2 == 65) { //move left "a"
                  imageNum1 = 2;
                  player1.setFacingRight(false); 
                  this.player1.setLeftVelocity();
            }

            if (var2 == 68) { //move right "d"
                  imageNum1 = 3; 
                  player1.setFacingRight(true); 
                  this.player1.setRightVelocity();
            }

            if (var2 == 87) { //jump "w"
                  imageNum1 = 4;
                  this.player1.jump();
            }

            if (var2 == 83) { //cancel jump "s"
                  imageNum1 = 1; 
                  this.player1.jumpCancel();
            }
            
            //Dash "r"
            if (var2 == 82 && p1DashCD <= 0) {
                  playSound("dash.wav");
                  if (player1.getXVelocity() > 0) {
                        player1.setX(player1.getX() + 500);
                  } else if (player1.getXVelocity() < 0) {
                        player1.setX(player1.getX() - 500);
                  } else if (player1.getYVelocity() < 0) {
                        player1.setY(player1.getY() - 400);
                  }

                  p1DashCD = 32; 
            }

            //Fireball attack "t"
            if (var2 == 84 && fireballCD <= 0) {
                  fireballCD = 48;
                  if (!player1.getFacingRight())
                  {
                        attackNum1 = 1;
                        player1.setXVelocity(0);
                        player1.setYVelocity(0);
                        attackP1.setAttackY(player1.getY());
                        attackP1.setAttackX(player1.getX() - 500);
                  } else {
                        attackNum1 = 2;
                        player1.setXVelocity(0);
                        player1.setYVelocity(0);
                        attackP1.setAttackY(player1.getY());
                        attackP1.setAttackX(player1.getX() + 500);
                  }
            }

            //Fire spin attack "g"
            if (var2 == 71 && fireSpinCD <= 0) {
                  autoAttackCancel1 = 8;
                  fireSpinCD = 80;
                  playSound("fireSpin.wav");
                  if (!player1.getFacingRight())
                  {
                        attackNum1 = 3;
                        player1.setXVelocity(0);
                        player1.setYVelocity(0);
                        attackP1.setAttackY(player1.getY() - 600);
                        attackP1.setAttackX(player1.getX() - 450);
                  } else {
                        attackNum1 = 4;
                        player1.setXVelocity(0);
                        player1.setYVelocity(0);
                        attackP1.setAttackY(player1.getY() - 600);
                        attackP1.setAttackX(player1.getX() - 100);
                  }
            }

            //Fire slash attack "v"
            if (var2 == 86 && fireSlashCD <= 0) {     
                  autoAttackCancel1 = 8;
                  fireSlashCD = 112;
                  playSound("fireSlash.wav");
                  if (!player1.getFacingRight())
                  {
                        attackNum1 = 5;
                        player1.setXVelocity(0);
                        player1.setYVelocity(0);
                        attackP1.setAttackY(player1.getY() - 400);
                        attackP1.setAttackX(player1.getX() - 450);
                  } else {
                        attackNum1 = 6;
                        player1.setXVelocity(0);
                        player1.setYVelocity(0);
                        attackP1.setAttackY(player1.getY() - 400);
                        attackP1.setAttackX(player1.getX() - 450);
                  }
            }

            //Player 2
            if (var2 == 37) { //left "left arrow"
                  imageNum2 = 2; 
                  this.player2.setLeftVelocity();
            }

            if (var2 == 39) { //right "right arrow"
                  imageNum2 = 3;
                  this.player2.setRightVelocity();
            }

            if (var2 == 38) { //jump "up arrow"
                  imageNum2 = 4; 
                  this.player2.jump();
            }

            if (var2 == 40) { //cancel jump "down arrow"
                  imageNum2 = 1; 
                  this.player2.jumpCancel();
            }

            //Dash "p"
            if (var2 == 80 && p2DashCD <= 0) {
                  playSound("dash.wav");
                  if (player2.getXVelocity() > 0) {
                        player2.setX(player2.getX() + 300);
                  } else if (player2.getXVelocity() < 0) {
                        player2.setX(player2.getX() - 300);
                  } else if (player2.getYVelocity() < 0) {
                        player2.setY(player2.getY() - 300);
                  }
                  p2DashCD = 16; 
            }

            //Flash/dash attack "o"
            if (var2 == 79 && flashCD <= 0) {
                  autoAttackCancel2 = 8;
                  flashCD = 24;
                  playSound("blackFlash.wav");
                  if (!player2.getFacingRight())
                  {
                        attackNum2 = 1;
                        player2.setX(player2.getX() - 100);
                        attackP2.setAttackY(player2.getY() - 10);
                        attackP2.setAttackX(player2.getX() - 500);
                  } else {
                        attackNum2 = 2;
                        player2.setX(player2.getX() + 100);
                        attackP2.setAttackY(player2.getY() - 10);
                        attackP2.setAttackX(player2.getX() + 500);
                  }
            }
            
            //Slash attack "k"
            if (var2 == 75 && swingCD <= 0) {
                  autoAttackCancel2 = 8;
                  swingCD = 48;
                  playSound("blackSlash.wav");
                  if (!player2.getFacingRight())
                  {
                        attackNum2 = 3;
                        player2.setXVelocity(0);
                        player2.setYVelocity(0);
                        attackP2.setAttackY(player2.getY());
                        attackP2.setAttackX(player2.getX() - 200);
                  } else {
                        attackNum2 = 4;
                        player2.setXVelocity(0);
                        player2.setYVelocity(0);
                        attackP2.setAttackY(player2.getY());
                        attackP2.setAttackX(player2.getX() + 200);
                  }
            }

            //Big slash "l"
            if (var2 == 76 && slashCD <= 0) {
                  autoAttackCancel2 = 8;
                  slashCD = 160;
                  playSound("hammerSlash.wav");
                  if (!player2.getFacingRight())
                  {
                        attackNum2 = 5;
                        player2.setXVelocity(0);
                        player2.setYVelocity(0);
                        attackP2.setAttackY(player2.getY() - 300);
                        attackP2.setAttackX(player2.getX() - 250);
                  } else {
                        attackNum2 = 6;
                        player2.setXVelocity(0);
                        player2.setYVelocity(0);
                        attackP2.setAttackY(player2.getY() - 300);
                        attackP2.setAttackX(player2.getX() - 200);
                  }
            }
      }

      //cancels things like movement and attack animation when key is no longer being pressed
      @Override
      public void keyReleased(KeyEvent var1) 
      {
            autoAttackCancel1 = 0;
            autoAttackCancel2 = 0;

            int var2 = var1.getKeyCode();
            if (var2 == 65) {
                  attackNum1 = 0;
                  this.player1.stopLeftVelocity();
            }

            if (var2 == 68) {
                  attackNum1 = 0;
                  this.player1.stopRightVelocity();
            }

            if (var2 == 37) {
                  attackNum1 = 0;
                  this.player2.stopLeftVelocity();
            }

            if (var2 == 39) {
                  attackNum1 = 0;
                  this.player2.stopRightVelocity();
            }

            if (var2 == 84 || var2 == 71 || var2 == 86) {
                  attackNum1 = 0;
                  imageNum1 = 1;
            }

            if (var2 == 79 || var2 == 76 || var2 == 75) {
                  attackNum2 = 0;
                  imageNum2 = 1;
            }

      }

      @Override
      public void keyTyped(KeyEvent var1) 
      {
            //Empty but this needs to exist
      }

      //health bar system
      public void drawHealthBar(Graphics g, int x, int y, double hp, double max, Color fill, String label) {
            int w = 350, h = 25;
            g.setColor(new Color(40, 40, 40)); // black background for health bar
            g.fillRect(x, y, w, h);

            if (hp > 0) {
                  g.setColor(fill); // Dynamic health color fill
                  g.fillRect(x + 2, y + 2, (int) ((w - 4) * (hp / max)), h - 4);
            }

            g.setColor(new Color(212, 175, 55)); // Metallic Gold Border
            g.drawRect(x, y, w, h);
            g.setColor(Color.WHITE); // Text Overlay
            g.drawString(label + ": " + (int)hp + "/" + (int)max, x + 10, y + 17);
      }

      public void drawComboBar(Graphics g, int x, int y, int charge, int maxCharge, Color fill, String label) {
            int sectionW = 52;
            int sectionH = 12;
            int spacing = 8;

            for (int i = 0; i < maxCharge; i++) {
                  int segmentX = x + i * (sectionW + spacing);
                  g.setColor(i < charge ? fill : new Color(100, 100, 100)); // gray color when not filled
                  g.fillRect(segmentX, y, sectionW, sectionH);
                  g.setColor(Color.WHITE);
                  g.drawRect(segmentX, y, sectionW, sectionH);
            }

            g.setColor(Color.WHITE);
            g.drawString(label + ": " + charge + "/" + maxCharge, x, y - 2);
      }

      private void drawCooldownCounter(Graphics g, int x, int y, String label) {
            int size = 36;
            //int seconds = Math.max(0, (int) Math.ceil(remainingFrames / 60.0));
            int seconds = 0; // Placeholder for cooldown logic

            //manages the cooldowns
            if (label.equals("Fireball")) {
                  seconds = Math.max(0, (int) (fireballCD / 16));
            } else if (label.equals("Fire spin")) {
                  seconds = Math.max(0, (int) (fireSpinCD / 16));
            } else if (label.equals("Fire slash")) {
                  seconds = Math.max(0, (int) (fireSlashCD / 16));
            } else if (label.equals("DashP1")) {
                  seconds = Math.max(0, (int) (p1DashCD / 16));
            } else if (label.equals("Flash")) {
                  seconds = Math.max(0, (int) (flashCD / 16));
            } else if (label.equals("Swing")) {
                  seconds = Math.max(0, (int) (swingCD / 16));
            } else if (label.equals("Slash")) {
                  seconds = Math.max(0, (int) (slashCD / 16));
            } else if (label.equals("DashP2")) {
                  seconds = Math.max(0, (int) (p2DashCD / 16));
            }

            String text = String.valueOf(seconds);

            //creates the circle that holds the counter
            g.setColor(new Color(20, 20, 20, 220));
            g.fillOval(x, y, size, size);
            g.setColor(new Color(212, 175, 55));
            g.drawOval(x, y, size, size);

            //labels each cooldown
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString(text, x + size / 2 - g.getFontMetrics().stringWidth(text) / 2, y + size / 2 + 6);

            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.setColor(new Color(230, 230, 230));
            int labelWidth = g.getFontMetrics().stringWidth(label);
            g.drawString(label, x + size / 2 - labelWidth / 2, y + size + 16);
      }

      //checks to see if the attack hit the other player
      private boolean isOverlapping(Box player, Attack attack) {
            return attack.getAttackX() + attack.getAttackLength() > player.getX() &&
                  attack.getAttackX() < player.getX() + player.getLength() &&
                  attack.getAttackY() + attack.getAttackHeight() > player.getY() &&
                  attack.getAttackY() < player.getY() + player.getHeight();
      }



      //ends the game and displays the winner
      private boolean matchEnded = false;
      private String endScreenWinnerLabel = "";

      private void verifyEndCondition() {
            if (!matchEnded) {
                  if (player1.getHp() <= 0) {
                        matchEnded = true;
                        endScreenWinnerLabel = "PLAYER 2 WINS";
                  } else if (player2.getHp() <= 0) {
                        matchEnded = true;
                        endScreenWinnerLabel = "PLAYER 1 WINS";
                  }
            }
      }

      //draws the game over screen with the winner and instructions to restart
      private void drawGameOver(Graphics g, int w, int h) {
            g.setColor(new Color(15, 15, 15, 245));
            g.fillRect(0, 0, w, h);
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // "GAME OVER"
            g2.setFont(new Font("Helvetica Neue", Font.BOLD, 80));
            g2.setColor(new Color(220, 50, 50));
            String mainTitle = "GAME OVER";
            g2.drawString(mainTitle, (w - g2.getFontMetrics().stringWidth(mainTitle)) / 2, h / 2 - 80);

            // Winner Subtitle
            g2.setFont(new Font("Helvetica Neue", Font.PLAIN, 32));
            g2.setColor(Color.WHITE);
            g2.drawString(endScreenWinnerLabel, (w - g2.getFontMetrics().stringWidth(endScreenWinnerLabel)) / 2, h / 2 + 10);

            // Subtext Instruction
            g2.setFont(new Font("Helvetica Neue", Font.ITALIC, 20));
            g2.setColor(new Color(140, 140, 140));
            String restartHint = "press ENTER to play again";
            g2.drawString(restartHint, (w - g2.getFontMetrics().stringWidth(restartHint)) / 2, h / 2 + 100);
      }

      //resets the match without needing to restart the program
      private void hardResetCurrentMatch() {
            try {
                  java.lang.reflect.Field hp1 = Box.class.getDeclaredField("hp");
                  hp1.setAccessible(true); 
                  hp1.set(player1, 1000.0);
                  java.lang.reflect.Field hp2 = Box.class.getDeclaredField("hp");
                  hp2.setAccessible(true); 
                  hp2.set(player2, 1000.0);
            } catch(Exception e) {} //it should never fail, but good to have the catch here anyways

            player1.setX(150); 
            player1.setY(250);
            player2.setX(this.getWidth() - 650); 
            player2.setY(250);
            attackNum1 = 0; 
            attackNum2 = 0; 
            comboCharge1 = 0;
            comboCharge2 = 0;
            matchEnded = false;
      }

      //system to make the damage numbers look cool
      private static class ScatterDamage {
            double x;
            double y;
            double driftX; // Left/Right drift vector
            int damageValue;
            int opacity = 255;

            ScatterDamage(int startX, int startY, int damageValue) {
                  // Apply a random horizontal scatter range of -40 to +40 pixels
                  this.driftX = (Math.random() * 4.0) - 2.0; 
                  this.x = startX + 160 + (Math.random() * 80 - 40); 
                  this.y = startY - 30;
                  this.damageValue = damageValue;
            }

            void updateState() {
                  this.y -= 2.5;             // Float upwards
                  this.x += this.driftX;     // Drift slightly sideways to spread out
                  this.opacity -= 6;         // Smooth fade tracking

                  if (this.opacity < 0) {
                        this.opacity = 0;
                  }
            }
      }

      //method that triggers the creation fot he damage numbers
      public void triggerVisualDamage(int targetX, int targetY, int value) {
            damageNumbers.add(new ScatterDamage(targetX, targetY, value));
      }

      //manages invulnerability frames and registers hits
      public void processRegulatedCombat() {
            // Ticks down frame cooldowns
            if (p1HitCooldown > 0) {
                  p1HitCooldown--;
            }

            if (p2HitCooldown > 0) {
                  p2HitCooldown--;
            }

            // Player 1 Attacks and Hits Player 2
            if (attackNum1 > 0 && p2HitCooldown == 0 && isOverlapping(player2, attackP1)) {
                  int calculatedDmg = 0;

                  // If combo is fully charged, do double damage and reset combo
                  if (comboCharge1 >= MAX_COMBO_CHARGE) {
                        if (attackNum1 == 1 || attackNum1 == 2) {
                              calculatedDmg = 50;
                        } else if (attackNum1 == 3 || attackNum1 == 4) {
                              calculatedDmg = 150;
                        } else if (attackNum1 == 5 || attackNum1 == 6) {
                              calculatedDmg = 150;
                        }

                        comboCharge1 = 0; // Reset combo after use
                  } else {
                  // Normal damage and increment combo
                        if (attackNum1 == 1 || attackNum1 == 2) {
                              calculatedDmg = 25;
                        } else if (attackNum1 == 3 || attackNum1 == 4) {
                              calculatedDmg = 75;
                        } else if (attackNum1 == 5 || attackNum1 == 6) {
                              calculatedDmg = 75;
                        }

                        comboCharge1++;
                  }

                  player2.takeDamage(calculatedDmg);
                  triggerVisualDamage(player2.getX(), player2.getY(), calculatedDmg);
                  p2HitCooldown = 20; // 20 frames of invulnerability so damage calculates cleanly
            }

            // Player 2 Attacks and Hits Player 1
            if (attackNum2 > 0 && p1HitCooldown == 0 && isOverlapping(player1, attackP2)) {
                  int calculatedDmg = 0;

                  // If combo is fully charged, do double damage and reset combo
                  if (comboCharge2 >= MAX_COMBO_CHARGE) {
                        if (attackNum2 == 1 || attackNum2 == 2) {
                              calculatedDmg = 50;
                        } else if (attackNum2 == 3 || attackNum2 == 4) {
                              calculatedDmg = 100;
                        } else if (attackNum2 == 5 || attackNum2 == 6) {
                              calculatedDmg = 400;
                              playSound("goofy-bonk.wav");
                              //Stun play 1 when hit with ult crit
                              player1.setXVelocity(0);
                              player1.setYVelocity(0);
                              fireSlashCD = 16;
                              fireSpinCD = 16;
                              fireballCD = 16;
                        }
                  comboCharge2 = 0; // Reset combo after use
                  } else {
                        // Normal damage and increment combo
                        if (attackNum2 == 1 || attackNum2 == 2) {
                              calculatedDmg = 25;
                        } else if (attackNum2 == 3 || attackNum2 == 4) {
                              calculatedDmg = 50;
                        } else if (attackNum2 == 5 || attackNum2 == 6) {
                              calculatedDmg = 200;
                        }

                        comboCharge2++;
                  }

                  slashCD -= 16; //hitting a skill reduces ultimate attack cooldown by 1 second
                  player1.takeDamage(calculatedDmg);
                  triggerVisualDamage(player1.getX(), player1.getY(), calculatedDmg);
                  p1HitCooldown = 20;
            }
      }

      //renders the damage numbers with a cool floating and fading effect
      private void renderScatteredDamage(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(new Font("Impact", Font.ITALIC, 42)); // Heavy arcade style font

            for (int i = damageNumbers.size() - 1; i >= 0; i--) {
                  ScatterDamage num = damageNumbers.get(i);
                  num.updateState();

                  if (num.opacity <= 0) {
                        damageNumbers.remove(i);
                  } else {
                        // Outer text drop shadow for visibility against moving backgrounds
                        g2.setColor(new Color(0, 0, 0, num.opacity));
                        g2.drawString("-" + num.damageValue, (int)num.x + 2, (int)num.y + 2);

                        // Main vibrant damage color
                        g2.setColor(new Color(235, 60, 60, num.opacity));
                        g2.drawString("-" + num.damageValue, (int)num.x, (int)num.y);
                  }
            }

            g2.dispose();
      }
}