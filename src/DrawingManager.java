//Handles the image changing and drawing of the players and their attacks for the game
package src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DrawingManager {
    //variables for all images
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

    //Constructor to load all images
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
        p2Attack3Left = loadImage("resources/images/Player_2_Roland/rolandSwingLeft.png");
        p2Attack3Right = loadImage("resources/images/Player_2_Roland/rolandSwingRight.png");
        worldSlashLeft = loadImage("resources/images/Player_2_Roland/worldCuttingSlashLeft.png");
        worldSlashRight = loadImage("resources/images/Player_2_Roland/worldCuttingSlashRight.png");

        noAttack = loadImage("resources/images/NoAttack.png");
    }

    //Helper method to load images and handle exceptions
    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Could not load image: " + path);
            e.printStackTrace();
            return null;
        }
    }

    //Methods to draw specific player and attack images based on the player number and image number
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

    //method that draws the player based on the player number and image number, 
    //and sets the player's height and length based on the image being drawn
    public void drawPlayer(Graphics g, Box player, int playerNumber, int imageNum) {
        if (player == null) {
            return;
        }

        BufferedImage image = (playerNumber == 2) ? player2BaseLeft : player1BaseRight; //Keeps the base form if no other form is needed
        
        if (playerNumber == 1) {
            if (imageNum == 0) {
                image = player1BaseLeft;
                player.setHeight(400);
                player.setLength(500);
            }else if (imageNum == 1) {
                image = player1BaseRight;
                player.setHeight(400);
                player.setLength(500);
            } else if (imageNum == 2) {
                image = player1Left;
                player.setHeight(400);
                player.setLength(500);
            } else if (imageNum == 3) {
                image = player1Right;
                player.setHeight(400);
                player.setLength(500);
            } else if (imageNum == 4) { //no seperate image numbers for going up left or right as this was added later on
                if (!player.getFacingRight())
                    {
                        image = player1UpLeft;
                    } else {
                        image = player1UpRight;
                    } 
                player.setHeight(400);
                player.setLength(500);
            } else if (imageNum == 5) {
                image = p1Attack1Left;
                player.setHeight(380);
                player.setLength(1000);
            } else if (imageNum == 6) {
                image = p1Attack1Right;
                player.setHeight(380);
                player.setLength(1000);
            } else if (imageNum == 7) {
                image = p1Attack2Left;
                player.setHeight(380);
                player.setLength(1000);
            } else if (imageNum == 8) {
                image = p1Attack2Right;
                player.setHeight(380);
                player.setLength(1000);
            } else if (imageNum == 9) {
                image = p1Attack3Left;
                player.setHeight(750);
                player.setLength(750);
            } else if (imageNum == 10) {
                image = p1Attack3Right;
                player.setHeight(750);
                player.setLength(750);
            }
        } else if (playerNumber == 2) {
            if (imageNum == 0) {
                image = player2BaseLeft;
                player.setHeight(275);
                player.setLength(100);
            }else if (imageNum == 1) {
                image = player2BaseRight;
                player.setHeight(275);
                player.setLength(100);
            } else if (imageNum == 2) {
                image = player2Left;
                player.setHeight(275);
                player.setLength(300);
            } else if (imageNum == 3) {
                image = player2Right;
                player.setHeight(275);
                player.setLength(300);
            } else if (imageNum == 4) {
                if (!player.getFacingRight())
                {
                    image = player2UpLeft;
                } else {
                    image = player2UpRight;
                } 
                    player.setHeight(250);
                    player.setLength(250);
            } else if (imageNum == 5) {
                image = p2Attack1Left;
                player.setHeight(200);
                player.setLength(400);
            } else if (imageNum == 6) {
                image = p2Attack1Right;
                player.setHeight(200);
                player.setLength(400);
            } else if (imageNum == 7) {
                image = p2Attack2Left;
                player.setHeight(200);
                player.setLength(500);
            } else if (imageNum == 8) {
                image = p2Attack2Right;
                player.setHeight(200);
                player.setLength(500);
            } else if (imageNum == 9) {
                image = p2Attack3Left;
                player.setHeight(250);
                player.setLength(250);
            } else if (imageNum == 10) {
                image = p2Attack3Right;
                player.setHeight(250);
                player.setLength(250);
            }
        }

        //actualy draws the image, or if the image is null it draws the default box
        if (image != null) {
            g.drawImage(image, player.getX(), player.getY(), player.getLength(), player.getHeight(), null);
        } else {
            player.draw(g);
        }
    }

    //method that draws the attack based on the player number and attack number,
    // and sets the attack's height and length based on the image being drawn.
    public void drawAttack(Graphics g, Attack attack, int playerNumber, int attackNum) {
        if (attack == null) {
            return;
        }

        BufferedImage image = noAttack; //Default to no attack image

        
        if (playerNumber == 1) {
            if (attackNum == 1) {
                image = fireSpearLeft;
                attack.setAttackHeight(400);
                attack.setAttackLength(750);
            } else if (attackNum == 2) {
                image = fireSpearRight;
                attack.setAttackHeight(400);
                attack.setAttackLength(750);
            } else if (attackNum == 3) {
                image = fireSpinLeft;
                attack.setAttackHeight(1000);
                attack.setAttackLength(1500);
            } else if (attackNum == 4) {
                image = fireSpinRight;
                attack.setAttackHeight(1000);
                attack.setAttackLength(1500);
            } else if (attackNum == 5) {
                image = fireSlashLeft;
                attack.setAttackHeight(1000);
                attack.setAttackLength(1500);
            } else if (attackNum == 6) {
                image = fireSlashRight;
                attack.setAttackHeight(1000);
                attack.setAttackLength(1500);
            }
        } else if (playerNumber == 2) 
         {
            if (attackNum == 1) {
                image = blackFlashLeft;
                attack.setAttackHeight(300);
                attack.setAttackLength(400);
            } else if (attackNum == 2) {
                image = blackFlashRight;
                attack.setAttackHeight(300);
                attack.setAttackLength(400);
            } else if (attackNum == 3) {
                image = blackSlashLeft;
                attack.setAttackHeight(300);
                attack.setAttackLength(500);
            } else if (attackNum == 4) {
                image = blackSlashRight;
                attack.setAttackHeight(300);
                attack.setAttackLength(500);
            } else if (attackNum == 5) {
                image = worldSlashLeft;
                attack.setAttackHeight(700);
                attack.setAttackLength(700);
            } else if (attackNum == 6) {
                image = worldSlashRight;
                attack.setAttackHeight(700);
                attack.setAttackLength(700);
            }
        }
    
        //goes back to blank attack
        if (attackNum == 0) {
            image = noAttack;
        }
    
        //draws the image, or if the image is null it draws the default box
        if (image != null) {
            g.drawImage(image, attack.getAttackX(), attack.getAttackY(), attack.getAttackLength(), attack.getAttackHeight(), null);
        } else {
            attack.draw(g);
        }
    }
}