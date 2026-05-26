package src;
import java.awt.Color;
import java.awt.Graphics;

public class Box {
    private int x = 250;
    private int y = 250;
    private int height = 250;
    private int length = 250;

    private int xVelocity = 0;
    private double yVelocity = 0;
    
    private final double GRAVITY = 1.3;
    private double JUMP_STRENGTH = -30.0;
    private int MOVE_SPEED = 15;
    private boolean isGrounded = false;

    public int getXVelocity() {
        return xVelocity;
    }

    public double getYVelocity() {
        return yVelocity;
    }

    public int getX() {
        return x;
    }

    public void setX(int newX) {
        x = newX;
    }

    public int getY() {
        return y;
    }

    public void setY(int newY) {
        y = newY;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int newHeight) {
        height = newHeight;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int newLength) {
        length = newLength;
    }

    public boolean getIsGrounded ()
    {
        return isGrounded;
    }

    public int getSpeed ()
    {
        return MOVE_SPEED;
    }

    public void setSpeed (int newSpeed)
    {
        MOVE_SPEED = newSpeed;
    }

    public void update(int panelWidth, int panelHeight, int groundY) {
        // Horizontal movement
        x += xVelocity;

        // Apply constant gravity
        yVelocity += GRAVITY;
        y += yVelocity;

        // Window edge collision boundaries
        if (x < 0)
        {
            x = 0;
        }

        if (x > panelWidth - length)
        {
            x = panelWidth - length;
        }

        // Ground collision detection
        if (y + height >= groundY)
        {
            y = groundY - height; 
            yVelocity = 0;             
            isGrounded = true;         
        } else 
        {
            isGrounded = false;
        }

        //Height limiter
        if (y < panelHeight*0.3)
            {
                JUMP_STRENGTH = 0.0;
            } else
            {
                JUMP_STRENGTH = -30.0;
            }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, length, height);
    }

    public void setLeftVelocity() {
        xVelocity = -MOVE_SPEED;
    }

    public void setRightVelocity() {
        xVelocity = MOVE_SPEED;
    }

    public void stopLeftVelocity() {
        if (xVelocity < 0) xVelocity = 0;
    }

    public void stopRightVelocity() {
        if (xVelocity > 0) xVelocity = 0;
    }

    public void jump() {
        if (isGrounded) {
            yVelocity = JUMP_STRENGTH;
            isGrounded = false;
        }
    }

    public void jumpCancel() {
        if (!isGrounded) {
            yVelocity = -JUMP_STRENGTH;
            isGrounded = true;
        }
    }
}