import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class SnakeGame {
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        new GameFrame();
    }

}