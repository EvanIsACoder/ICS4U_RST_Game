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
   public int p2DashCD = 0;

   public int fireballCD = 0;

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
                  System.out.println("✅ 从物理路径抓到了图: " + path);
                  return ImageIO.read(file);
            } else {
                  System.out.println("❌ 还是没找到: " + file.getAbsolutePath());
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
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      p1DashCD--;
      p2DashCD--;
      fireballCD--;

      this.player1.update(this.getWidth(), panelHeight, panelHeight - 400);
      this.player2.update(this.getWidth(), panelHeight, panelHeight - 400);
/*    
      if (attackNum1 == 1) {
          imageNum1 = 5;
      } else if (attackNum1 == 2)
      {
          imageNum1 = 6;
      }

      if (imageNum1 == 0 ||imageNum1 == 1 )
      {
            player1.setHeight(300);
            player1.setLength(1000);
      }if (imageNum1 == 4)
      {
            player1.setHeight(460); 
            player1.setLength(500);  
      } else if (imageNum1 == 2 || imageNum1 == 3)
      {
            player1.setLength(500);
      } else if (imageNum1 == 5 || imageNum1 == 6) {
            player1.setLength(700);
      } else
      {
            player1.setLength(250);
            player1.setHeight(250);
      }
      

      if (player1.getXVelocity() == 0 && player1.getYVelocity() == 0)
      {
            if (player1.getX() > player2.getX())
            {
                  imageNum1 = 0;
            } else
            {
                  imageNum1 = 1;
            }
      } */

      if (imageNum2 == 1)
      {
            player2.setLength(100);   
      } else 
      {
            player2.setLength(250);
      }

      if (fireballCD > 0 && attackNum1 == 1) {
          attackP1.setAttackX(attackP1.getAttackX() - 30);
      } else if (fireballCD > 0 && attackNum1 == 2) {
            attackP1.setAttackX(attackP1.getAttackX() + 30);
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

      //Fireball attack
      if (var2 == 84 && fireballCD <= 0) {
            if (player1.getXVelocity() < 0)
            {
                  attackNum1 = 1;
                  fireballCD = 48;
                  attackP1.setAttackY(player1.getY());
                  attackP1.setAttackX(player1.getX() - 500);
                  player1.setXVelocity(0);
                  player1.setYVelocity(0);
            } else if (player1.getXVelocity() > 0)
            {
                  attackNum1 = 2;
                  fireballCD = 48;
                  attackP1.setAttackY(player1.getY());
                  attackP1.setAttackX(player1.getX() + 500);
                  player1.setXVelocity(0);
                  player1.setYVelocity(0);
            }
      }

      //

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

      // testing to stop drawing attack
      if (var2 == 84) {
            attackNum1 = 0;
      }
    }

   @Override
   public void keyTyped(KeyEvent var1) 
   {
        //Empty but this needs to exist
   }
}
