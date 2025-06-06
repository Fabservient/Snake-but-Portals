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
    private final int[] xa = {1,0,-1,0};
    private final int[] ya = {0,1,0,-1};
    private ArrayList<Point> snake;
    private Point apple;
    private int score = 0;
    private boolean gameRunning = false;

    public SnakeGame() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(new Color(170, 215, 81));
        setFocusable(true);
        addKeyListener(this);
        snake = new ArrayList<>();
        snake.add(new Point(9, 8));
        snake.add(new Point(8, 8));
        snake.add(new Point(7, 8));
        timer = new Timer(500,this);
        timer.start();
        JButton startButton = new JButton("Start Game");
        setLayout(null);
        startButton.setBounds(130, 190, 140, 40); // x, y, width, height
        add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameRunning = true;
                startButton.setVisible(false);
                repaint();
            }
        });
        spawnApple();
    }

    private void spawnApple() {
        while (true) {
            int x = (int)(Math.random()*COLS);
            int y = (int)(Math.random()*ROWS);
            Point p = new Point(x, y);
            if (!snake.contains(p)) {
                apple = p;
                break;
            }
        }
    }
    
    private void updateTimer(){
        if(score > 5){
            timer.setDelay(400);
        }
        else if(score > 10){
            timer.setDelay(300);
        }
        else if(score > 20){
            timer.setDelay(200);
        }
        else if(score > 30){
            timer.setDelay(100);
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
        
        if (!gameRunning) {
            drawStartScreen(g);
        }
    }

     private void drawSnake(Graphics g) {
        g.setColor(Color.BLUE);
        //Drawing the front
        //Filling the 'middle'
        for (Point p : snake) {
            g.fillRoundRect(p.x * TILE_SIZE + 22, p.y * TILE_SIZE + 82, TILE_SIZE-4, TILE_SIZE-4,15,15);
        }
        //Filling connections
        for(int i = 0; i < snake.size()-1; i++){
            Point p1 = snake.get(i); Point p2 = snake.get(i+1);
            g.fillRect((p1.x+p2.x)*TILE_SIZE/2+22,(p1.y+p2.y)*TILE_SIZE/2+82,TILE_SIZE-4,TILE_SIZE-4);
        }
    }

    private void drawApple(Graphics g) {
        g.setColor(new Color(220,50,50));
        g.fillOval(apple.x * TILE_SIZE + 22, apple.y * TILE_SIZE + 80+3, TILE_SIZE-4, TILE_SIZE-5);
        g.setColor (new Color(101,67,33));
        g.fillRect(apple.x * TILE_SIZE + 20 + 10, apple.y * TILE_SIZE + 82,2,6);
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
        //g.setColor(Color.GREEN); //changed color for visibility
        g.setColor(new Color(74,117,44));
        g.fillRoundRect(80, 150, 240, 120,30,30);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20)); //changed font size to fit better
        //g.drawString("Move Mouse to Start!", 100, 200);
    }

    private void drawEndScreen(Graphics g){
        //AYAAN DO THIS
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameRunning) {
            repaint();
            return;
        }
        Point head = new Point(snake.get(0));
        head.x += xa[direction];
        head.y += ya[direction];
        
         if (head.x < 0 || head.x >= COLS || head.y < 0 || head.y >= ROWS || snake.contains(head)) {
            timer.stop();
            repaint();
            return;
        }
        snake.add(0,head);
        if(head.equals(apple)){
            score++;
            updateTimer();
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
        if (key == KeyEvent.VK_LEFT && direction != 0) direction = 2;
        if (key == KeyEvent.VK_RIGHT && direction != 2) direction = 0;
        if (key == KeyEvent.VK_UP && direction != 1) direction = 3;
        if (key == KeyEvent.VK_DOWN && direction != 3) direction = 1;
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
