//This class manages the attack boxes for the game
package src;
import java.awt.Color;
import java.awt.Graphics;

public class Attack {
    //variables for placement on screen
    private int attackX = 250;
    private int attackY = 250;

    //size variables
    private int attackHeight = 400;
    private int attackLength = 750;

    //getters and setters for the attack box variables
    public int getAttackX() {
        return attackX;
    }

    public void setAttackX(int newX) {
        attackX = newX;
    }

    public int getAttackY() {
        return attackY;
    }

    public void setAttackY(int newY) {
        attackY = newY;
    }

    public int getAttackHeight() {
        return attackHeight;
    }

    public void setAttackHeight(int newHeight) {
        attackHeight = newHeight;
    }

    public int getAttackLength() {
        return attackLength;
    }

    public void setAttackLength(int newLength) {
        attackLength = newLength;
    }

    //backup draw method for the attack box (when the images aren't working)
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(attackX, attackY, attackLength, attackHeight);
    }
}