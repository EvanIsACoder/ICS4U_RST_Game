package src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DrawingManager {
    private BufferedImage player1BaseLeft;
    private BufferedImage player1BaseRight;
    private BufferedImage player1Left;
    private BufferedImage player1Right;
    private BufferedImage player1UpLeft;
    private BufferedImage player1UpRight;
    private BufferedImage p1Attack1Left;
    private BufferedImage p1Attack1Right;
    private BufferedImage fireSpearLeft;
    private BufferedImage fireSpearRight;
    private BufferedImage p1Attack2Left;
    private BufferedImage p1Attack2Right;
    private BufferedImage fireSpinLeft;
    private BufferedImage fireSpinRight;
    private BufferedImage p1Attack3Left;
    private BufferedImage p1Attack3Right;
    private BufferedImage fireSlashLeft;
    private BufferedImage fireSlashRight;


    private BufferedImage player2BaseLeft;
    private BufferedImage player2BaseRight;
    private BufferedImage player2Left;
    private BufferedImage player2Right;
    private BufferedImage player2UpLeft;
    private BufferedImage player2UpRight;
    private BufferedImage p2Attack1Left;
    private BufferedImage p2Attack1Right;
    private BufferedImage blackFlashLeft;
    private BufferedImage blackFlashRight;
    private BufferedImage p2Attack2Left;
    private BufferedImage p2Attack2Right;
    private BufferedImage blackSlashLeft;
    private BufferedImage blackSlashRight;
    private BufferedImage p2Attack3Left;
    private BufferedImage p2Attack3Right;
    private BufferedImage worldSlashLeft;
    private BufferedImage worldSlashRight;

    private BufferedImage noAttack;

    public DrawingManager() {
        player1BaseLeft = loadImage("resources/images/Player_1_Xiao/xiaoBaseLeft.png");
        player1BaseRight = loadImage("resources/images/Player_1_Xiao/xiaoBaseRight(1).png");
        player1Left = loadImage("resources/images/Player_1_Xiao/xiaoDashLeft.png");
        player1Right = loadImage("resources/images/Player_1_Xiao/xiaoDashRight.png");
        player1UpLeft = loadImage("resources/images/Player_1_Xiao/xiaoUpLeft.png");
        player1UpRight = loadImage("resources/images/Player_1_Xiao/xiaoUpRight.png");
        p1Attack1Left = loadImage("resources/images/Player_1_Xiao/xiaoFugaLeft.png");
        p1Attack1Right = loadImage("resources/images/Player_1_Xiao/xiaoFugaRight.png");
        fireSpearLeft = loadImage("resources/images/Player_1_Xiao/fugaLeft.png");
        fireSpearRight = loadImage("resources/images/Player_1_Xiao/fugaRight.png");
        p1Attack2Left = loadImage("resources/images/Player_1_Xiao/xiaoChargingLeft.png");
        p1Attack2Right = loadImage("resources/images/Player_1_Xiao/xiaoChargingRight.png");
        fireSpinLeft = loadImage("resources/images/Player_1_Xiao/fugaSpinLeft.png");
        fireSpinRight = loadImage("resources/images/Player_1_Xiao/fugaSpinRight.png");
        p1Attack3Left = loadImage("resources/images/Player_1_Xiao/xiaoSlashLeft.png");
        p1Attack3Right = loadImage("resources/images/Player_1_Xiao/xiaoSlashRight.png");
        fireSlashLeft = loadImage("resources/images/Player_1_Xiao/worldCuttingFugaLeft.png");
        fireSlashRight = loadImage("resources/images/Player_1_Xiao/worldCuttingFugaRight.png");

        player2BaseLeft = loadImage("resources/images/Player_2_Roland/rolandBaseLeft.png");
        player2BaseRight = loadImage("resources/images/Player_2_Roland/rolandBaseRight.png");
        player2Left = loadImage("resources/images/Player_2_Roland/rolandDashLeft.png");
        player2Right = loadImage("resources/images/Player_2_Roland/rolandDashRight.png");
        player2UpRight = loadImage("resources/images/Player_2_Roland/rolandUpRight.png");
        player2UpLeft = loadImage("resources/images/Player_2_Roland/rolandUpLeft.png");
        p2Attack1Left = loadImage("resources/images/Player_2_Roland/rolandFlashLeft.png");
        p2Attack1Right = loadImage("resources/images/Player_2_Roland/rolandFlashRight.png");
        blackFlashLeft = loadImage("resources/images/Player_2_Roland/blackFlashLeft.png");
        blackFlashRight = loadImage("resources/images/Player_2_Roland/blackFlashRight.png");
        p2Attack2Left = loadImage("resources/images/Player_2_Roland/rolandSlashLeft.png");
        p2Attack2Right = loadImage("resources/images/Player_2_Roland/rolandSlashRight.png");
        blackSlashLeft = loadImage("resources/images/Player_2_Roland/blackSlashLeft.png");
        blackSlashRight = loadImage("resources/images/Player_2_Roland/blackSlashRight.png");
        p2Attack3Left = loadImage("resources/images/Player_2_Roland/rolandSlashLeft.png");
        p2Attack3Right = loadImage("resources/images/Player_2_Roland/rolandSlashRight.png");
        worldSlashLeft = loadImage("resources/images/Player_2_Roland/worldCuttingSlashLeft.png");
        worldSlashRight = loadImage("resources/images/Player_2_Roland/worldCuttingSlashRight.png");

        noAttack = loadImage("resources/images/NoAttack.png");
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

        BufferedImage image = (playerNumber == 2) ? player2BaseLeft : player1BaseRight; //Keeps the base form if no other form is needed
        
        if (playerNumber == 1) {
            if (imageNum == 0)
            {
                image = player1BaseLeft;
            }else if (imageNum == 1) 
            {
                image = player1BaseRight;
            } else if (imageNum == 2) 
            {
                image = player1Left;
            } else if (imageNum == 3) 
            {
                image = player1Right;
            } else if (imageNum == 4) 
            {
                image = player1UpRight;
            } else if (imageNum == 5)
            {
                image = p1Attack1Left;
            } else if (imageNum == 6)
            {
                image = p1Attack1Right;
            }
        } else if (playerNumber == 2) {
            if (imageNum == 1) 
            {
                image = player2BaseRight;
            } else if (imageNum == 2) 
            {
                image = player2Left;
            } else if (imageNum == 3) 
            {
                image = player2Right;
            } else if (imageNum == 4) 
            {
                image = player2UpRight;
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

        /*
        if (playerNumber == 1) 
        {
            if (attackNum == 1) 
            {
                image = fireballLeft;
            } else if (attackNum == 2)
            {
                image = fireballRight;
            }
        } else if (playerNumber == 2) 
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