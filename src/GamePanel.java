package src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;        // <--- 核心补丁：告诉 Java 什么是 File
import java.io.IOException; // <--- 核心补丁：告诉 Java 什么是 IOException
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

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

   public int imageNum1;
   public int imageNum2;
   public int attackNum1;
   public int attackNum2;

   public int p1DashCD = 0;
   public int autoAttackCancel1 = 0;
   public int p2DashCD = 0;
   public int autoAttackCancel2 = 0;

   public int fireballCD = 0;
   public int fireSpinCD = 0;
   public int fireSlashCD = 0;

   public int comboCharge1 = 0;
   public int comboCharge2 = 0;
   private static final int MAX_COMBO_CHARGE = 4;
   private boolean p1ComboReady = false;
   private boolean p2ComboReady = false;
   private boolean p1AttackHitRegistered = false;
   private boolean p2AttackHitRegistered = false;

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
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
   
   public GamePanel() {
        bgWall = loadImage("/images/Texture2D/Xiao_Background_Wall.png");
        bgFloor = loadImage("/images/Texture2D/Xiao_Background_Floor.png");
        bgWall2 = loadImage("/images/Texture2D/Xiao_Background_Wall2.png");
        
        mainCamFrame = loadImage("/images/Texture2D/MainCam_Frame.png");
        battleFrame = loadImage("/images/Texture2D/frame.png");
        lowerFrame = loadImage("/images/Texture2D/OverlayUI_LowerFrame.png");
        topFrame = loadImage("/images/Texture2D/Base (상하조절가능).png");

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
        this.drawingManager = new DrawingManager();
        Timer var1 = new Timer(16, this);
        var1.start();
   }

   private Image loadImage(String path) {
      try {
            // 方案 A: 尝试类路径加载
            URL url = getClass().getResource(path);
            if (url != null) return ImageIO.read(url);

            // 方案 B: 物理路径保底 (针对并行的 resources 文件夹)
            // 这里的 File 就需要 import java.io.File
            File file = new File("resources" + path); 
            if (file.exists()) {
                  return ImageIO.read(file);
            }
      } catch (IOException e) { // 这里的 IOException 就需要 import java.io.IOException
            e.printStackTrace();
      }
      return null;
}

/* 辅助小方法：确保路径斜杠正确
private String urlAdjust(String path) {
      return path.replace("\\", "/");
  }*/

   @Override
   protected void paintComponent(Graphics var1) {
        super.paintComponent(var1);


        int screenW = getWidth();
        int screenH = getHeight();

        if (isGameOver) {
            paintDeathOverlay(var1, screenW, screenH);
        }

       // 1. Sky
       if (bgWall != null){
            var1.drawImage(bgWall, 0, 0, screenW, screenH, this);
       }

       // 2. Floor
       if (bgFloor != null) {
           double ratio = (double) screenW / bgFloor.getWidth(null);
           int drawH = (int) (bgFloor.getHeight(null) * ratio);
           int y = (int) (screenH * 0.004); 
           var1.drawImage(bgFloor, 0, y, screenW, drawH, this);
           
           if (bgWall2 != null) {
               double wallRatio = (double) screenW / bgWall2.getWidth(null);
               int wallDrawH = (int) (bgWall2.getHeight(null) * wallRatio);
               int wallY = y + (int) (drawH * 0.72); 
               var1.drawImage(bgWall2, 0, wallY, screenW, wallDrawH, this);
           }
       }

        //var1.setColor(new Color(19, 19, 19));
        //var1.fillRect(0, panelHeight - 200, this.getWidth(), 200);
        this.drawingManager.drawPlayer1(var1, this.player1, imageNum1);
        this.drawingManager.drawPlayer2(var1, this.player2, imageNum2);
        this.drawingManager.drawAttack1(var1, this.attackP1, attackNum1);
        //this.drawingManager.drawAttack2(var1, this.attackP2, attackNum2);

        // 3. UI 边框 (按顺序叠放)
		if (mainCamFrame != null) {
			// 设定你想要的缩进距离，比如四周各缩进屏幕宽度的 5%
			// 你可以根据感觉调整这个 0.05
			int paddingW = (int)(screenW * 0.01); 
			int paddingH = (int)(screenH * 0.01);

			// 计算实际绘制的位置和大小
			int frameX = paddingW;
			int frameY = paddingH;
			int frameW = screenW - (paddingW * 2);
			int frameH = screenH - (paddingH * 2);
		
			// 这样画出来的框就会往中间靠，露出外面的一圈
			var1.drawImage(mainCamFrame, frameX, frameY, frameW, frameH, this);
		}
		
        if (battleFrame != null){
            var1.drawImage(battleFrame, 0, 0, screenW, screenH, this);
        }
        
		if (lowerFrame != null) {
		// 【1】等比例计算放大后的高度
		// 假设我们让它稍微比屏幕宽一点点，或者就等于屏幕宽
		double lowerRatio = (double) screenW / lowerFrame.getWidth(null);
		int lowerDrawH = (int) (lowerFrame.getHeight(null) * lowerRatio);
    
		// 【2】计算 Y 坐标实现贴底
		// 如果直接画在 0, 0，金框会在中间悬着。
		// 我们让 y = 屏幕高度 - 图片放大后的高度
		// 这样图片的底边就死死贴住了屏幕底边
		int lowerY = screenH - lowerDrawH;

		// 【3】如果觉得金框还是太靠上（因为原图下方有留白），可以加个“微调值”
		// 比如 lowerY + 10 让它再往下沉一点，藏进边框里
		var1.drawImage(lowerFrame, 0, lowerY+10, screenW, lowerDrawH, this);
		}

		if (topFrame != null) {
			// 依然采用等比例放大，保证它铺满左右宽度
			double topRatio = (double) screenW / topFrame.getWidth(null);
			int topDrawH = (int) (topFrame.getHeight(null) * topRatio);
			// 坐标 (0, 0) 就是死死贴住顶部
			var1.drawImage(topFrame, 0, 0, screenW, topDrawH, this);
		}
            if (topFrame != null) {
              double topRatio = (double) screenW / topFrame.getWidth(null);
              int topDrawH = (int) (topFrame.getHeight(null) * topRatio);
              var1.drawImage(topFrame, 0, 0, screenW, topDrawH, this);
        }

//Health bars
        drawBetterBar(var1, 50, 55, player1.getHp(), player1.getMaxHp(), new Color(46, 204, 113), "P1");
        drawBetterBar(var1, screenW - 400, 55, player2.getHp(), player2.getMaxHp(), new Color(231, 76, 60), "P2");
        int comboSectionW = 52;
        int comboSpacing = 8;
        int comboTotalWidth = MAX_COMBO_CHARGE * comboSectionW + (MAX_COMBO_CHARGE - 1) * comboSpacing;
        drawComboBar(var1, 50, 90, comboCharge1, MAX_COMBO_CHARGE, new Color(255, 255, 255), "COMBO1");
        drawComboBar(var1, screenW - 50 - comboTotalWidth, 90, comboCharge2, MAX_COMBO_CHARGE, new Color(255, 255, 255), "COMBO2");

        if (matchEnded) {
            drawMinimalistGameOver(var1, screenW, screenH);
        }
   }

@Override
   public void actionPerformed(ActionEvent var1) {

if (matchEnded) { this.repaint(); return; }
        verifyEndCondition();

      p1DashCD--;
      fireballCD--;
      fireSpinCD--;
      fireSlashCD--;
      autoAttackCancel1--;

      p2DashCD--;
      autoAttackCancel2--;

      if (isGameOver) { this.repaint(); return; }
        checkMatchStatus();

      this.player1.update(this.getWidth(), panelHeight, panelHeight - 400);
      this.player2.update(this.getWidth(), panelHeight, panelHeight - 400);

      checkAttackHits(); 

   this.player1.update(this.getWidth(), panelHeight, panelHeight - 400);
   this.player2.update(this.getWidth(), panelHeight, panelHeight - 400);
 

      if (player1.getXVelocity() == 0 && player1.getYVelocity() == 0 && attackNum1 == 0)
      {
            if (player1.getX() > player2.getX())
            {
                  imageNum1 = 0;
            } else {
                  imageNum1 = 1;
            }
      }

      if (player2.getXVelocity() == 0 && player2.getYVelocity() == 0 && attackNum2 == 0)
            {
                  if (player2.getX() > player1.getX())
                  {
                        imageNum2 = 0;
                  } else {
                        imageNum2 = 1;
                  }
            }

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

      if (fireballCD > 0 && attackNum1 == 1) {
          attackP1.setAttackX(attackP1.getAttackX() - 30);
      } else if (fireballCD > 0 && attackNum1 == 2) {
            attackP1.setAttackX(attackP1.getAttackX() + 30);
      }

      if (autoAttackCancel1 == 0)
      {
            attackNum1 = 0;
      }

      if (autoAttackCancel2 == 0)
            {
                  attackNum2 = 0;
            }
  
      this.repaint();
   }

   @Override
   public void keyPressed(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      
      if (matchEnded) {
            if (var2 == KeyEvent.VK_ENTER) { hardResetCurrentMatch(); }
            return;
        }

      if (isGameOver) {
            if (var2 == KeyEvent.VK_ENTER) { restartGame(); }
            return;
        }

      //Player 1
      if (var2 == 65) {
            imageNum1 = 2;
            player1.setFacingRight(false); 
            this.player1.setLeftVelocity();
      }

      if (var2 == 68) {
            imageNum1 = 3; 
            player1.setFacingRight(true); 
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

      //Fireball attack
      if (var2 == 84 && fireballCD <= 0) {
            if (comboCharge1 >= MAX_COMBO_CHARGE) {
                  p1ComboReady = true;
                  comboCharge1 = 0;
            }
            p1AttackHitRegistered = false;
            if (!player1.getFacingRight())
            {
                  attackNum1 = 1;
                  fireballCD = 48;
                  player1.setXVelocity(0);
                  player1.setYVelocity(0);
                  attackP1.setAttackY(player1.getY());
                  attackP1.setAttackX(player1.getX() - 500);
            } else
            {
                  attackNum1 = 2;
                  fireballCD = 48;
                  player1.setXVelocity(0);
                  player1.setYVelocity(0);
                  attackP1.setAttackY(player1.getY());
                  attackP1.setAttackX(player1.getX() + 500);
            }
      }

      //Fire spin attack
      while (var2 == 71 && fireSpinCD <= 0)
      {
            if (comboCharge1 >= MAX_COMBO_CHARGE) {
                  p1ComboReady = true;
                  comboCharge1 = 0;
            }
            autoAttackCancel1 = 8;
            p1AttackHitRegistered = false;
            if (!player1.getFacingRight())
            {
                  attackNum1 = 3;
                  fireSpinCD = 24;
                  player1.setXVelocity(0);
                  player1.setYVelocity(0);
                  attackP1.setAttackY(player1.getY() - 600);
                  attackP1.setAttackX(player1.getX() - 450);
            } else
            {
                  attackNum1 = 4;
                  fireSpinCD = 24;
                  player1.setXVelocity(0);
                  player1.setYVelocity(0);
                  attackP1.setAttackY(player1.getY() - 600);
                  attackP1.setAttackX(player1.getX() - 100);
            }
      }

      //Fire slash attack
      if (var2 == 86 && fireSlashCD <= 0)
      {     
            if (comboCharge1 >= MAX_COMBO_CHARGE) {
                  p1ComboReady = true;
                  comboCharge1 = 0;
            }
            autoAttackCancel1 = 8;
            p1AttackHitRegistered = false;

            if (!player1.getFacingRight())
            {
                  attackNum1 = 5;
                  fireSlashCD = 64;
                  player1.setXVelocity(0);
                  player1.setYVelocity(0);
                  attackP1.setAttackY(player1.getY() - 400);
                  attackP1.setAttackX(player1.getX() - 450);
            } else
            {
                  attackNum1 = 6;
                  fireSlashCD = 64;
                  player1.setXVelocity(0);
                  player1.setYVelocity(0);
                  attackP1.setAttackY(player1.getY() - 400);
                  attackP1.setAttackX(player1.getX() - 450);
            }
      }

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

      //Dash
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

      //Dash attack
      if (var2 == 79)
      {
            if (comboCharge2 >= MAX_COMBO_CHARGE) {
                  p2ComboReady = true;
                  comboCharge2 = 0;
            }
            autoAttackCancel2 = 8;
            p2AttackHitRegistered = false;
            if (player2.getFacingRight())
            {
                  attackNum2 = 1;
                  player2.setX(player2.getX() + 100);
                  attackP2.setAttackY(player2.getY() - 100);
                  attackP2.setAttackX(player2.getX() + 250);
            } else
            {
                  attackNum2 = 2;
                  player2.setX(player2.getX() - 100);
                  attackP2.setAttackY(player2.getY() - 100);
                  attackP2.setAttackX(player2.getX() - 250);
            }
      }
      
      //Slasha ttack
      if (var2 == 76)
      {
            if (comboCharge2 >= MAX_COMBO_CHARGE) {
                  p2ComboReady = true;
                  comboCharge2 = 0;
            }
            autoAttackCancel2 = 8;
            p2AttackHitRegistered = false;
            if (player2.getFacingRight())
            {
                  attackNum2 = 3;
            } else
            {
                  attackNum2 = 4;
            }
      }

      //Big slash
      if (var2 == 75)
      {
            if (comboCharge2 >= MAX_COMBO_CHARGE) {
                  p2ComboReady = true;
                  comboCharge2 = 0;
            }
            autoAttackCancel2 = 8;
            p2AttackHitRegistered = false;
            if (player2.getFacingRight())
            {
                  attackNum2 = 5;
            } else
            {
                  attackNum2 = 6;
            }
      }
   }

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

      // testing to stop drawing attack
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

// --- POLISHED HEALTH BAR SYSTEM ---
    public void drawBetterBar(Graphics g, int x, int y, double hp, double max, Color fill, String label) {
        int w = 350, h = 25;
        g.setColor(new Color(40, 40, 40)); // Sleek dark track
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
            g.setColor(i < charge ? fill : new Color(100, 100, 100));
            g.fillRect(segmentX, y, sectionW, sectionH);
            g.setColor(Color.WHITE);
            g.drawRect(segmentX, y, sectionW, sectionH);
        }

        g.setColor(Color.WHITE);
        g.drawString(label + ": " + charge + "/" + maxCharge, x, y - 6);
    }

// --- COLLISION LOGIC ---
    private void checkAttackHits() {
        // If Player 1 is attacking, check if hitboxes overlap Player 2
        if (attackNum1 > 0 && isOverlapping(player2, attackP1) && !p1AttackHitRegistered) {
            int damage = 0;

            if (attackNum1 == 1 || attackNum1 == 2)
            {
                  damage = 25;
            } else if (attackNum1 == 3 || attackNum1 == 4)
            {
                  damage = 50;
            } else if (attackNum1 == 5 || attackNum1 == 6)
            {
                  damage = 200;
            }

            if (p1ComboReady) {
                damage *= 2;
                p1ComboReady = false;
            } else {
                comboCharge1 = Math.min(MAX_COMBO_CHARGE, comboCharge1 + 1);
            }
            player2.takeDamage(damage);
            p1AttackHitRegistered = true;
        }
        // If Player 2 is attacking, check if hitboxes overlap Player 1
        if (attackNum2 > 0 && isOverlapping(player1, attackP2) && !p2AttackHitRegistered) {
            int damage = 0;
            if (attackNum2 == 1 || attackNum2 == 2)
                  {
                        damage = 25;
                  } else if (attackNum2 == 3 || attackNum2 == 4)
                  {
                        damage = 50;
                  } else if (attackNum2 == 5 || attackNum2 == 6)
                  {
                        damage = 200;
                  }

            if (p2ComboReady) {
                damage *= 2;
                p2ComboReady = false;
            } else {
                comboCharge2 = Math.min(MAX_COMBO_CHARGE, comboCharge2 + 1);
            }
            player1.takeDamage(damage);
            p2AttackHitRegistered = true;
        }
    }

    private boolean isOverlapping(Box player, Attack attack) {
        return attack.getAttackX() + attack.getAttackLength() > player.getX() &&
               attack.getAttackX() < player.getX() + player.getLength() &&
               attack.getAttackY() + attack.getAttackHeight() > player.getY() &&
               attack.getAttackY() < player.getY() + player.getHeight();
    }

// --- DEATH SCREEN EXTENSION ---
    private boolean isGameOver = false;
    private String winnerText = "";

    private void checkMatchStatus() {
        if (!isGameOver) {
            if (player1.getHp() <= 0) {
                isGameOver = true;
                winnerText = "PLAYER 2 WINS!";
            } else if (player2.getHp() <= 0) {
                isGameOver = true;
                winnerText = "PLAYER 1 WINS!";
            }
        }
    }

    private void paintDeathOverlay(Graphics g, int w, int h) {
        g.setColor(new Color(0, 0, 0, 230));
        g.fillRect(0, 0, w, h);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        g.setColor(new Color(240, 50, 50));
        g.drawString(winnerText, (w - g.getFontMetrics().stringWidth(winnerText)) / 2, h / 2 - 30);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.setColor(Color.WHITE);
        String sub = "Press ENTER to Restart";
        g.drawString(sub, (w - g.getFontMetrics().stringWidth(sub)) / 2, h / 2 + 50);
    }

    private void restartGame() {
        try {
            java.lang.reflect.Field hp1 = Box.class.getDeclaredField("hp");
            hp1.setAccessible(true); hp1.set(player1, 1000.0);
            java.lang.reflect.Field hp2 = Box.class.getDeclaredField("hp");
            hp2.setAccessible(true); hp2.set(player2, 1000.0);
        } catch(Exception e) {}
        player1.setX(150); player1.setY(250);
        player2.setX(this.getWidth() - 650); player2.setY(250);
        attackNum1 = 0; attackNum2 = 0;
        isGameOver = false;
    }

// --- UNIQUE CONFIGURATION FOR SIMPLISTIC GAME OVER ---
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

    private void drawMinimalistGameOver(Graphics g, int w, int h) {
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
        String restartHint = "press ENTER twice to play again";
        g2.drawString(restartHint, (w - g2.getFontMetrics().stringWidth(restartHint)) / 2, h / 2 + 100);
    }

    private void hardResetCurrentMatch() {
        try {
            java.lang.reflect.Field hp1 = Box.class.getDeclaredField("hp");
            hp1.setAccessible(true); hp1.set(player1, 100.0);
            java.lang.reflect.Field hp2 = Box.class.getDeclaredField("hp");
            hp2.setAccessible(true); hp2.set(player2, 100.0);
        } catch(Exception e) {}
        player1.setX(150); player1.setY(250);
        player2.setX(this.getWidth() - 650); player2.setY(250);
        attackNum1 = 0; attackNum2 = 0; comboCharge1 = 0; comboCharge2 = 0;
        matchEnded = false;
    }
}