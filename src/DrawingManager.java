package src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DrawingManager {
    private BufferedImage player1Base;
    private BufferedImage player1Left;
    private BufferedImage player1Right;
    private BufferedImage player1Up;
    private BufferedImage p1Attack1Left;

    private BufferedImage player2Base;
    private BufferedImage player2Left;
    private BufferedImage player2Right;
    private BufferedImage player2Up;
    private BufferedImage p2Attack1;

    private BufferedImage noAttack;
    private BufferedImage fireball;

    public DrawingManager() {
        player1Base = loadImage("resources/images/Player_1_Xiao/XiaoBase.png");
        player1Left = loadImage("resources/images/Player_1_Xiao/XiaoLeft.png");
        player1Right = loadImage("resources/images/Player_1_Xiao/XiaoRight.png");
        player1Up = loadImage("resources/images/Player_1_Xiao/XiaoUp.png");
        p1Attack1Left = loadImage("resources/images/Player_1_Xiao/XiaoAttack1Left.png");

        player2Base = loadImage("resources/images/Player_2_Roland/RolandBase.png");
        player2Left = loadImage("resources/images/Player_2_Roland/RolandLeft.png");
        player2Right = loadImage("resources/images/Player_2_Roland/RolandRight.png");
        player2Up = loadImage("resources/images/Player_2_Roland/RolandUp.png");
        //p2Attack1 = loadImage("resources/images/Player_2_Roland/RolandAttack1.png");

        noAttack = loadImage("resources/images/NoAttack.png");
        fireball = loadImage("resources/images/fireball.png");
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Could not load image: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public void drawPlayer1(Graphics g, Box player, int imageNum1) {
        drawPlayer(g, player, 1, imageNum1);
    }

    public void drawAttack1(Graphics g, Attack attack, int attack1) {
        drawAttack(g, attack, 1, attack1);
    }

    public void drawPlayer2(Graphics g, Box player, int imageNum2) {
        drawPlayer(g, player, 2, imageNum2);
    }

    public void drawAttack2(Graphics g, Attack attack, int attack2) {
        drawAttack(g, attack, 2, attack2);
    }

    public void drawPlayer(Graphics g, Box player, int playerNumber, int imageNum) {
        if (player == null) {
            return;
        }

        BufferedImage image = (playerNumber == 2) ? player2Base : player1Base; //Keeps the base form if no other form is needed
        
        if (playerNumber == 1) {
            if (imageNum == 1) 
            {
                image = player1Base;
            } else if (imageNum == 2) 
            {
                image = player1Left;
            } else if (imageNum == 3) 
            {
                image = player1Right;
            } else if (imageNum == 4) 
            {
                image = player1Up;
            } else if (imageNum == 5)
            {
                image = p1Attack1Left;
            }
        } else if (playerNumber == 2) {
            if (imageNum == 1) 
            {
                image = player2Base;
            } else if (imageNum == 2) 
            {
                image = player2Left;
            } else if (imageNum == 3) 
            {
                image = player2Right;
            } else if (imageNum == 4) 
            {
                image = player2Up;
            }
        }

        if (image != null) {
            g.drawImage(image, player.getX(), player.getY(), player.getLength(), player.getHeight(), null);
        } else {
            player.draw(g);
        }
    }

    public void drawAttack(Graphics g, Attack attack, int playerNumber, int attackNum) {
        if (attack == null) {
            return;
        }

        BufferedImage image = noAttack; //Default to no attack image

        if (playerNumber == 1) 
        {
            if (attackNum == 1) 
            {
                image = fireball;
            }
        } /*else if (playerNumber == 2) 
         {
             if (attackNum == 1) 
            {
                 image = p2Attack1;
            }
        }*/

            if (attackNum == 0)
            {
                image = noAttack;
            }
    
        if (image != null) {
            g.drawImage(image, attack.getAttackX(), attack.getAttackY(), attack.getAttackLength(), attack.getAttackHeight(), null);
        }
    }
}