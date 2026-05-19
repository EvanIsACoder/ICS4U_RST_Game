package src;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DrawingManager {
    private BufferedImage xiao_base;
    private Box player1;
    private Box player2;

    public DrawingManager() {
        try {
            // This path works perfectly from ANY java package folder 
            // as long as the 'images' folder is in your root project folder
            xiao_base = ImageIO.read(new File("images/Player_1/XiaoBase.png"));
        } catch (IOException e) {
            System.out.println("Could not find file from inside the graphics folder.");
            e.printStackTrace();
        }
        
        player1 = new Box(50, 100, xiao_base);
        player2 = new Box(200, 100, xiao_base);
    }
    
}