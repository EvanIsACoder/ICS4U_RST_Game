package src;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    //Variables for the game background
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;
    private static final int GROUND_Y = 350;

    //Creates two separate player box objects
    private final Box player1;
    private final Box player2;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true); 

        addKeyListener(this);

        //Instantiates both players
        player1 = new Box(); 
        player2 = new Box(); // Note: They will overlap at spawn (X=100) initially
        
        // 60 Frames Per Second loop ticker
        Timer timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw Environment Floor
        g.setColor(new Color(19, 19, 19));
        g.fillRect(0, GROUND_Y, getWidth(), HEIGHT - GROUND_Y);

        // 3. Render both distinct objects
        //player1.draw(g);

        player1.draw(g);
        player2.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 4. Run physics engine updates independently on both objects
        player1.update(getWidth(), GROUND_Y);
        player2.update(getWidth(), GROUND_Y);
        repaint(); 
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // --- Player 1 Controls (Arrow Keys) ---
        if (code == KeyEvent.VK_LEFT) player1.setLeftVelocity();
        if (code == KeyEvent.VK_RIGHT) player1.setRightVelocity();
        if (code == KeyEvent.VK_UP) player1.jump();
        if (code == KeyEvent.VK_DOWN) player1.jumpCancel();

        // --- Player 2 Controls (WASD Keys) ---
        if (code == KeyEvent.VK_A) player2.setLeftVelocity();
        if (code == KeyEvent.VK_D) player2.setRightVelocity();
        if (code == KeyEvent.VK_W) player2.jump();
        if (code == KeyEvent.VK_S) player2.jumpCancel();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        // --- Player 1 Key Releases ---
        if (code == KeyEvent.VK_LEFT) player1.stopLeftVelocity();
        if (code == KeyEvent.VK_RIGHT) player1.stopRightVelocity();

        // --- Player 2 Key Releases ---
        if (code == KeyEvent.VK_A) player2.stopLeftVelocity();
        if (code == KeyEvent.VK_D) player2.stopRightVelocity();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}