import javax.swing.*;

public class GameFrame extends JFrame {
    GamePanel gamePanel;
    public GameFrame() {
        gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setSize(1300,750);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("SnakeGame by N");
    }
}
