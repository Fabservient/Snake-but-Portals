import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

//This is experimental code
public class SnakeGame extends JPanel implements ActionListener, KeyListener{

    private Timer timer;
    private final int TILE_SIZE = 20;
    private final int ROWS = 15; // changed from 15
    private final int COLS = 18;
    private int direction = 0; // 0=right, 1=down, 2=left, 3=up
    private int nextDirection = 0;
    private final int[] xa = {1,0,-1,0};
    private final int[] ya = {0,1,0,-1};
    private ArrayList<Point> snake;
    private Point apple1;
    private Point apple2;
    private int score = 0;
    private int gameRunning = 0;// 0 = start 1 = running 2 = end

    public SnakeGame() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(new Color(170, 215, 81));
        setFocusable(true);
        addKeyListener(this);
        snake = new ArrayList<>();
        snake.add(new Point(9, 8));
        snake.add(new Point(8, 8));
        snake.add(new Point(7, 8));
        timer = new Timer(200,this);
        timer.start();
        JButton startButton = new JButton("Start Game");
        setLayout(null);
        startButton.setBounds(130, 250, 140, 40); // Moved button down by changing y from 190 to 250
        add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameRunning = 1;
                startButton.setVisible(false);
                repaint();
            }
        });
        spawnApple();
    }

    private void spawnApple() {
        // Spawn apple1
        while (true) {
            int x1 = (int)(Math.random() * COLS);
            int y1 = (int)(Math.random() * ROWS);
            Point p1 = new Point(x1, y1);
            if (!snake.contains(p1)) {
                apple1 = p1;
                break;
            }
        }

        // Spawn apple2
        while (true) {
            int x2 = (int)(Math.random() * COLS);
            int y2 = (int)(Math.random() * ROWS);
            Point p2 = new Point(x2, y2);
            if (!snake.contains(p2) && !p2.equals(apple1)) {
                apple2 = p2;
                break;
            }
        }
    }


    private void updateTimer(){
        if(score > 5){
            timer.setDelay(150);
        }
        else if(score > 10){
            timer.setDelay(125);
        }
        else if(score > 20){
            timer.setDelay(100);
        }
        else if(score > 30){
            timer.setDelay(75);
        }
        else if(score > 45){
            timer.setDelay(50);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCheckerboard(g);
        drawBorders(g);
        drawSnake(g);
        drawApple(g);

        if (gameRunning == 0) {
            drawStartScreen(g);
        }
        else if (gameRunning == 2)
        {
            drawEndScreen(g);
        }
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.BLUE);
        // Draw body segments
        for (Point p : snake) {
            g.fillRoundRect(p.x * TILE_SIZE + 22, p.y * TILE_SIZE + 82, TILE_SIZE - 4, TILE_SIZE - 4, 15, 15);
        }

        // Draw connections only between adjacent segments
        for (int i = 0; i < snake.size() - 1; i++) {
            Point p1 = snake.get(i);
            Point p2 = snake.get(i + 1);
            int dx = Math.abs(p1.x - p2.x);
            int dy = Math.abs(p1.y - p2.y);
            if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
                g.fillRect((p1.x + p2.x) * TILE_SIZE / 2 + 22, (p1.y + p2.y) * TILE_SIZE / 2 + 82, TILE_SIZE - 4, TILE_SIZE - 4);
            }
            else{
                g.setColor(new Color(182, 95, 207));
                g.fillOval(p1.x*TILE_SIZE+19, p1.y*TILE_SIZE+79,TILE_SIZE+2,TILE_SIZE+2);
                g.fillOval(p2.x*TILE_SIZE+19, p2.y*TILE_SIZE+79,TILE_SIZE+2,TILE_SIZE+2);
                g.setColor(Color.BLUE);
                g.fillOval(p1.x*TILE_SIZE+22, p1.y*TILE_SIZE+82,TILE_SIZE-4,TILE_SIZE-4);
                g.fillOval(p2.x*TILE_SIZE+22, p2.y*TILE_SIZE+82,TILE_SIZE-4,TILE_SIZE-4);
                if(!p1.equals(snake.get(0))){
                    Point p0 = snake.get(i-1);
                    g.fillRect((p1.x + p0.x) * TILE_SIZE / 2 + 22, (p1.y + p0.y) * TILE_SIZE / 2 + 82, TILE_SIZE - 4, TILE_SIZE - 4);
                }
            }
        }

        //draw eyes + head
        int hcx = snake.get(0).x*TILE_SIZE+30+5*xa[direction];
        int hcy = snake.get(0).y*TILE_SIZE+90+5*ya[direction];
        g.fillRoundRect(hcx-8,hcy-8,TILE_SIZE-4,TILE_SIZE-4,15,15);
        g.setColor(Color.WHITE);
        g.fillOval(hcx+5*xa[direction]+5*ya[direction]-3,hcy+5*ya[direction]+5*xa[direction]-3,6,6);
        g.fillOval(hcx+5*xa[direction]-5*ya[direction]-3,hcy+5*ya[direction]-5*xa[direction]-3,6,6);
        g.setColor(Color.BLACK);
        g.fillOval(hcx+6*xa[direction]+5*ya[direction]-2,hcy+6*ya[direction]+5*xa[direction]-2,4,4);
        g.fillOval(hcx+6*xa[direction]-5*ya[direction]-2,hcy+6*ya[direction]-5*xa[direction]-2,4,4);
        //draw eyes

    }


    private void drawApple(Graphics g) {
        g.setColor(new Color(220,50,50));
        g.fillOval(apple1.x * TILE_SIZE + 22, apple1.y * TILE_SIZE + 80+3, TILE_SIZE-4, TILE_SIZE-5);
        g.setColor (new Color(101,67,33));
        g.fillRect(apple1.x * TILE_SIZE + 20 + 10, apple1.y * TILE_SIZE + 82,2,6);
        g.setColor(new Color(220,50,50));
        g.fillOval(apple2.x * TILE_SIZE + 22, apple2.y * TILE_SIZE + 80+3, TILE_SIZE-4, TILE_SIZE-5);
        g.setColor (new Color(101,67,33));
        g.fillRect(apple2.x * TILE_SIZE + 20 + 10, apple2.y * TILE_SIZE + 82,2,6);
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
                g.fillRect(x * TILE_SIZE + 20, y * TILE_SIZE + 80, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void drawBorders(Graphics g)
    {

        g.setColor(new Color(87, 138, 52));

        g.fillRect(0, 80, 20, 320); //left
        g.fillRect(380, 80, 20, 320); //right
        g.fillRect(0, 60, 400, 20); //top
        g.fillRect(20, 380, 360, 20); //bottom
        g.setColor(new Color(74,117,44));
        g.fillRect(0,0,400,60); //top header

    }


    private void drawStartScreen(Graphics g) {
        g.setColor(new Color(74,117,44));
        g.fillRoundRect(80, 150, 240, 160, 30, 30);

        // snake code
        //snake
        g.setColor(Color.BLUE);
        g.fillRoundRect(100, 200, 20, 20, 15, 15);
        g.fillRoundRect(120, 200, 20, 20, 15, 15);
        g.fillRoundRect(140, 200, 20, 20, 15, 15);
        g.fillRoundRect(160, 200, 20, 20, 15, 15);
        //eyes
        g.setColor(Color.WHITE);
        g.fillOval(165, 205, 6, 6);
        g.fillOval(175, 205, 6, 6);
        //pupils
        g.setColor(Color.BLACK);
        g.fillOval(166, 206, 4, 4);
        g.fillOval(176, 206, 4, 4);
        //apple
        g.setColor(new Color(220,50,50));
        g.fillOval(190, 200, 20, 20);
        //stem
        g.setColor(new Color(101,67,33));
        g.fillRect(200, 195, 2, 6);
        //arrow keys
        g.setColor(new Color(180, 180, 180));
        // up box
        g.fillRoundRect(250, 185, 28, 28, 6, 6);
        // other boxes
        g.fillRoundRect(220, 215, 28, 28, 6, 6); // left
        g.fillRoundRect(250, 215, 28, 28, 6, 6); // down
        g.fillRoundRect(280, 215, 28, 28, 6, 6); // right

        g.setColor(Color.WHITE);
        // actual up triangle
        g.fillPolygon(new int[]{256, 264, 272}, new int[]{205, 191, 205}, 3);
        // down triangle
        g.fillPolygon(new int[]{256, 264, 272}, new int[]{223, 237, 223}, 3);
        // left triangle
        g.fillPolygon(new int[]{240, 226, 240}, new int[]{221, 229, 237}, 3);
        // right triangle
        g.fillPolygon(new int[]{288, 302, 288}, new int[]{221, 229, 237}, 3);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Snake but with Portals", 93,175);

    }

    private void drawEndScreen(Graphics g){
        //AYAAN DO THIS\
        g.setColor(new Color(74,117,44));
        g.fillRoundRect(80, 150, 240, 100, 30, 30); //background box
        //snake
        g.setColor(Color.BLUE);
        g.fillRoundRect(100, 200, 20, 20, 15, 15);
        g.fillRoundRect(120, 200, 20, 20, 15, 15);
        g.fillRoundRect(140, 200, 20, 20, 15, 15);
        g.fillRoundRect(160, 200, 20, 20, 15, 15);
        //eyes
        g.setColor(Color.WHITE);
        g.fillOval(165, 205, 6, 6);
        g.fillOval(175, 205, 6, 6);
        //pupils
        g.setColor(Color.BLACK);
        g.fillOval(166, 206, 4, 4);
        g.fillOval(176, 206, 4, 4);
        //apple
        g.setColor(new Color(220,50,50));
        g.fillOval(190, 200, 20, 20);
        //stem
        g.setColor(new Color(101,67,33));
        g.fillRect(200, 195, 2, 6);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD,20));
        g.drawString("Game Over", 93,175);
        g.drawString("Score: " + String.valueOf(score),220,175);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning == 0) {
            repaint();
            return;
        }
        if (gameRunning == 2) {
            repaint();
            return;
        }

        // Only allow one direction change per frame
        direction = nextDirection;

        Point head = new Point(snake.get(0));
        head.x += xa[direction];
        head.y += ya[direction];

        if (head.x < 0 || head.x >= COLS || head.y < 0 || head.y >= ROWS || snake.contains(head)) {
            //timer.stop();
            gameRunning = 2;
            repaint();
            return;
        }
        snake.add(0,head);
        if(head.equals(apple1)){
            score++;
            updateTimer();
            snake.set(0, apple2);
            spawnApple();
        }
        if(head.equals(apple2)){
            score++;
            updateTimer();
            snake.set(0, apple1);
            spawnApple();
        }
        else{
            snake.remove(snake.size()-1);
        }
        repaint();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && direction != 0) nextDirection = 2;
        if (key == KeyEvent.VK_RIGHT && direction != 2) nextDirection = 0;
        if (key == KeyEvent.VK_UP && direction != 1) nextDirection = 3;
        if (key == KeyEvent.VK_DOWN && direction != 3) nextDirection = 1;
    }


    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        //JButton startButton = new JButton("Start Game");
        SnakeGame gamePanel = new SnakeGame();

        // gamePanel.add(startButton);
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
