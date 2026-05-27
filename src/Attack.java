package src;
import java.awt.Color;
import java.awt.Graphics;

public class Attack {
    private int attackX = 250;
    private int attackY = 250;
    private int attackHeight = 500;
    private int attackLength = 850;

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

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(attackX, attackY, attackLength, attackHeight);
    }
}
