package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JPanel implements ActionListener, KeyListener {
    // Game Constants
    private final int WIDTH = 800, HEIGHT = 600;
    private final int PADDLE_WIDTH = 15, PADDLE_HEIGHT = 100;
    private final int BALL_SIZE = 20;

    // Game State
    private int player1Y = 250, player2Y = 250;
    private int ballX = WIDTH / 2, ballY = HEIGHT / 2;
    private int ballXDir = -2, ballYDir = 3;
    private int score1 = 0, score2 = 0;

    // Movement flags
    private boolean wPressed, sPressed, upPressed, downPressed;

    public PongGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);
        this.setFocusable(true);

        // Game Loop: triggers every 10ms (~100 FPS)
        Timer timer = new Timer(10, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);

        // Draw Center Line
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

        // Draw Paddles
        g.fillRect(20, player1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(WIDTH - 20 - PADDLE_WIDTH, player2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw Ball
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Draw Score
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString(String.valueOf(score1), WIDTH / 4, 50);
        g.drawString(String.valueOf(score2), (WIDTH / 4) * 3, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Move Paddles
        if (wPressed && player1Y > 0) player1Y -= 5;
        if (sPressed && player1Y < HEIGHT - PADDLE_HEIGHT) player1Y += 5;
        if (upPressed && player2Y > 0) player2Y -= 5;
        if (downPressed && player2Y < HEIGHT - PADDLE_HEIGHT) player2Y += 5;

        // Move Ball
        ballX += ballXDir;
        ballY += ballYDir;

        // Bounce off top and bottom
        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) ballYDir = -ballYDir;

        // Paddle Collision Logic
        Rectangle ballRect = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);
        Rectangle p1Rect = new Rectangle(20, player1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        Rectangle p2Rect = new Rectangle(WIDTH - 20 - PADDLE_WIDTH, player2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        if (ballRect.intersects(p1Rect) || ballRect.intersects(p2Rect)) {
            ballXDir = -ballXDir;
            // Increase speed slightly after each hit
            ballXDir += (ballXDir > 0) ? 1 : -1;
        }

        // Scoring
        if (ballX < 0) {
            score2++;
            resetBall();
        } else if (ballX > WIDTH) {
            score1++;
            resetBall();
        }

        repaint();
    }

    private void resetBall() {
        ballX = WIDTH / 2;
        ballY = HEIGHT / 2;
        ballXDir = (ballXDir > 0) ? -2 : 2; // Send to the loser
    }

    // Input Handling
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) wPressed = true;
        if (code == KeyEvent.VK_S) sPressed = true;
        if (code == KeyEvent.VK_UP) upPressed = true;
        if (code == KeyEvent.VK_DOWN) downPressed = true;
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) wPressed = false;
        if (code == KeyEvent.VK_S) sPressed = false;
        if (code == KeyEvent.VK_UP) upPressed = false;
        if (code == KeyEvent.VK_DOWN) downPressed = false;
    }

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Swing Pong - 2 Player");
        PongGame game = new PongGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}