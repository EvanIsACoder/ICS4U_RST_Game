package src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DrawingManager {
    private BufferedImage player1Base;
    //private BufferedImage player1Right;

    private BufferedImage player2Base;

    public DrawingManager() {
        player1Base = loadImage("resources/images/Player_1_Xiao/XiaoBase.png");
        //player1Right = loadImage("resources/images/Player_1_Xiao/XiaoRight.png");

        player2Base = loadImage("resources/images/Player_2_Roland/邵EGO蒲牢鳴锺1SD.png");
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

    public void drawPlayer1(Graphics g, Box player) {
        drawPlayer(g, player, 1);
    }

    public void drawPlayer2(Graphics g, Box player) {
        drawPlayer(g, player, 2);
    }

    public void drawPlayer(Graphics g, Box player, int playerNumber) {
        if (player == null) {
            return;
        }

        BufferedImage image = (playerNumber == 2) ? player2Base : player1Base;

        if (image != null) {
            g.drawImage(image, player.getX(), player.getY(), player.getSize(), player.getSize(), null);
        } else {
            player.draw(g);
        }
    }
}