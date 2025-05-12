import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

//This is experimental code
public class SnakeGame extends JPanel implements ActionListener, KeyListener, MouseMotionListener {

    private final int TILE_SIZE = 20;
    private final int ROWS = 15; // changed from 15
    private final int COLS = 18;
    private boolean gameRunning = false;
    
    public SnakeGame() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(new Color(170, 215, 81));
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCheckerboard(g);
        drawBorders(g);

        if (!gameRunning) {
            drawStartScreen(g);
        }
    }

    private void drawCheckerboard(Graphics g) {
        Color evenColor = new Color(162, 209, 73);
        Color oddColor = new Color(170, 215, 81);
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                if ((x + y) % 2 == 0)
                    g.setColor(evenColor);
                else
                    g.setColor(oddColor);
                g.fillRect(x * TILE_SIZE + 20, y * TILE_SIZE + 20, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void drawBorders(Graphics g)
    {

        g.setColor(new Color(87, 138, 52));
        /** OLD CODE FOR BORDERS
        g.fillRect(0, 60, 20, 340);//left
        g.fillRect(380, 60, 20, 340);//right
        g.fillRect(20, 60, 360, 20); //top
        g.fillRect(20, 380, 360, 20);//bottom
        //g.setColor(new Color(74, 117, 44));
        //g.fillRect(0, 30, 400, 60);
         **/
        //new code for borders
        g.fillRect(0, 0, 20, 400);
        g.fillRect(380, 0, 20, 400);
        g.fillRect(20, 0, 360, 20);
        g.fillRect(20, 380, 360, 20);
    }

    private void drawStartScreen(Graphics g) {
        g.setColor(Color.GREEN); //changed color for visibility
        g.fillRect(80, 140, 240, 120);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20)); //changed font size to fit better
        g.drawString("Move Mouse to Start!", 100, 200);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameRunning) {
            repaint();
            return;
        }
    }
    @Override public void keyPressed(KeyEvent e) {}
    
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame gamePanel = new SnakeGame();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
