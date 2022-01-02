import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import java.io.IOException;


public class GameFrame extends JFrame {

    GameFrame() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        GamePanel gamePanel =new GamePanel();
this.add(gamePanel);
this.setTitle("Snake Game");
this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
this.pack();
this.setVisible(true);
this.setIconImage(gamePanel.snakeImage.getImage());
this.setResizable(false);
this.setLocationRelativeTo(null);

    }

}
