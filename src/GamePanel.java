import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {
    final int WIDTH = 1300;
    final int HEIGHT = 750;
    final int UNIT_SIZE = 40;
    char direction = 'R';
    List<Integer> snakeBodyX;
    List<Integer> snakeBodyY;
    boolean running;
    int bodyParts = 3;
    int applesEaten = 0;
    int appleX;
    int appleY;
    Random random;
    Thread thread;

    public GamePanel() {
        random = new Random();
        snakeBodyX = new ArrayList<>();
        snakeBodyY = new ArrayList<>();
        this.setFocusable(true);
        setInitialSnake();
        running = true;
        newApple();
        this.addKeyListener(new MyKeyAdapter());
        thread = new Thread(this);
        thread.start();
    }

    private void setInitialSnake() {
        snakeBodyX.add(UNIT_SIZE * 2);
        snakeBodyY.add(0);
        snakeBodyX.add(UNIT_SIZE);
        snakeBodyY.add(0);
        snakeBodyX.add(0);
        snakeBodyY.add(0);
    }

    private void move() {
        int x = snakeBodyX.get(0);
        int y = snakeBodyY.get(0);
        switch(direction) {
            case 'U':
                if (snakeBodyY.get(0) - UNIT_SIZE >= 0) {
                    snakeBodyY.set(0,snakeBodyY.get(0) - UNIT_SIZE);
                } else {
                    gameOver();
                }
                break;
            case 'D':
                if (snakeBodyY.get(0) + UNIT_SIZE < HEIGHT - UNIT_SIZE) {
                    snakeBodyY.set(0,snakeBodyY.get(0) + UNIT_SIZE);
                } else {
                    gameOver();
                }
                break;
            case 'L':
                if (snakeBodyX.get(0) - UNIT_SIZE >= 0) {
                    snakeBodyX.set(0, snakeBodyX.get(0) - (UNIT_SIZE * 2));
                } else {
                    gameOver();
                }
            case 'R':
                if (snakeBodyX.get(0) + UNIT_SIZE < WIDTH) {
                    snakeBodyX.set(0, snakeBodyX.get(0) + UNIT_SIZE);
                } else {
                    gameOver();
                }
        }
            for (int i = 1; i < bodyParts; i++) {
                int x2 = snakeBodyX.get(i);
                snakeBodyX.set(i,x);
                x = x2;
                int y2 = snakeBodyY.get(i);
                snakeBodyY.set(i,y);
                y = y2;
            }
    }
    private void checkCollision() {
        int snakeHeadX = snakeBodyX.get(0);
        int snakeHeadY = snakeBodyY.get(0);
        for (int i = 1; i < bodyParts; i++) {
            if(snakeHeadX == snakeBodyX.get(i) && snakeHeadY == snakeBodyY.get(i)) {
                gameOver();
            }
        }
    }
    private void gameOver() {
        JOptionPane.showMessageDialog(this, "YOUR SCORE: " + applesEaten, "Game over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
    private void newApple(){
        appleX = random.nextInt(WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(HEIGHT/UNIT_SIZE)*UNIT_SIZE;
        while (snakeBodyY.contains(appleY) || snakeBodyX.contains(appleX) || appleY >= HEIGHT - UNIT_SIZE || appleX >= WIDTH - UNIT_SIZE) {
            appleX = random.nextInt(WIDTH/UNIT_SIZE)*UNIT_SIZE ;
            appleY = random.nextInt(HEIGHT/UNIT_SIZE)*UNIT_SIZE;
        }
    }


    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(Color.red);
        g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.green);
                g.fillRect(snakeBodyX.get(0), snakeBodyY.get(0), UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(0, 143, 50));
                g.fillRect(snakeBodyX.get(i), snakeBodyY.get(i), UNIT_SIZE, UNIT_SIZE);
            }
        }
        g.setColor(Color.red);
        Font myFont = new Font("Serif", Font.BOLD, 50);
        g.setFont(myFont);
        g.drawString("Score : " + applesEaten, 50, 60);
    }
    private void eatenApple() {
        if (snakeBodyY.get(0) == appleY && snakeBodyX.get(0) == appleX) {
            bodyParts++;
            snakeBodyX.add(snakeBodyX.get(bodyParts - 2));
            snakeBodyY.add(snakeBodyY.get(bodyParts - 2));
            applesEaten++;
            newApple();
        }
    }

    @Override
    public void run() {
        while (running) {
            move();
            eatenApple();
            checkCollision();
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
