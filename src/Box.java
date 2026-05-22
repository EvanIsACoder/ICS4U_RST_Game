package src;
import java.awt.Color;
import java.awt.Graphics;

public class Box {
    private int x = 100;
    private int y = 100;
    private final int size = 100;

    private int xVelocity = 0;
    private double yVelocity = 0;
    
    private final double GRAVITY = 1.0;
    private double JUMP_STRENGTH = -15.0;
    private final int MOVE_SPEED = 5;
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

    public int getSize() {
        return size;
    }

    public boolean getIsGrounded ()
    {
        return isGrounded;
    }

    public void update(int panelWidth, int groundY) {
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

        if (x > panelWidth - size)
        {
            x = panelWidth - size;
        }

        // Ground collision detection
        if (y + size >= groundY)
        {
            y = groundY - size; 
            yVelocity = 0;             
            isGrounded = true;         
        } else 
        {
            isGrounded = false;
        }

        //Height limiter
        if (y < 100)
            {
                JUMP_STRENGTH = 0.0;
            } else
            {
                JUMP_STRENGTH = -15.0;
            }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, size, size);
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