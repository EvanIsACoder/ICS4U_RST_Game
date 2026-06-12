package src;
import java.awt.Color;
import java.awt.Graphics;

public class Box {
    //health bar variables
    private double hp = 1000.0;
    private final double MAX_HP = 1000.0;
    private long lastHitTime = 0; // Tracks invincibility frames
    private final long INVINCIBILITY_COOLDOWN = 500; // 0.5 seconds in milliseconds

    //variables for placement on screen
    private int x = 250;
    private int y = 250;

    //size variables
    private int height = 250;
    private int length = 250;

    //movement variables
    private int xVelocity = 0;
    private double yVelocity = 0;
    
    //advanced movement variables
    private final double GRAVITY = 1.3;
    private double JUMP_STRENGTH = -30.0;
    private int MOVE_SPEED = 15;
    private boolean isGrounded = false;
    private boolean facingRight; // Is used, just not really in this file

    //gets and setters for important variables needed to be accessed in other files
    public int getXVelocity() {
        return xVelocity;
    }

    public void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }    

    public double getYVelocity() {
        return yVelocity;
    }

    public void setYVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
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

    public boolean getIsGrounded () {
        return isGrounded;
    }

    public int getSpeed () {
        return MOVE_SPEED;
    }

    public void setSpeed (int newSpeed) {
        MOVE_SPEED = newSpeed;
    }

    public boolean getFacingRight () {
        if (xVelocity > 0)
        {
            return true;
        }
        return false;
    }

    public void setFacingRight (boolean directionIsRight) {
        facingRight = directionIsRight;
    }

    public void update(int panelWidth, int panelHeight, int groundY) {
        // Horizontal movement
        x += xVelocity;

        // Apply constant gravity
        yVelocity += GRAVITY;
        y += yVelocity;

        // Window edge collision boundaries
        if (x < 0) {
            x = 0;
        }

        if (x > panelWidth - length) {
            x = panelWidth - length;
        }

        // Ground collision detection
        if (y + height >= groundY)
        {
            y = groundY - height; 
            yVelocity = 0;             
            isGrounded = true;         
        } else {
            isGrounded = false;
        }

        //Height limiter
        if (y < panelHeight*0.3) {
            JUMP_STRENGTH = 0.0;
        } else {
            JUMP_STRENGTH = -30.0;
        }
    }

    //Draws the box (backup for when the images aren't working)
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, length, height);
    }

    //methods to manage the movement
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

    //getters and setters for the health system
    public double getHp() {
        return hp;
    }

    public double getMaxHp() {
        return MAX_HP;
    }

    //manages damage
    public void takeDamage(int amount) {
        long currentTime = System.currentTimeMillis();
        // Only take damage if the invincibility cooldown has passed
        if (currentTime - lastHitTime >= INVINCIBILITY_COOLDOWN) {
            this.hp -= amount;

            if (this.hp < 0) {
                this.hp = 0;
            }
            
            lastHitTime = currentTime; // Reset the invincibility timer
        }
    }
}