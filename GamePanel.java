import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Random;
import static java.lang.String.valueOf;
public class GamePanel extends JPanel implements ActionListener {
    File loseFile= new File("Res/lose.wav");
    File pickupFIle= new File("Res/pickup.wav");
    File bgMusic= new File("Res/Space Age Hustle - Squadda B.wav");

    AudioInputStream audioInputStream;
    AudioInputStream audioInputStream2;
    AudioInputStream audioInputStream3;

    Clip loseClip;
    Clip pickupCLip;
    Clip bgMusicClip;
    JLabel countdownLabel = new JLabel("00:00");
    int NM_score;
    int HM_score;

    Timer t;
    int Timer= 0;
    ImageIcon snakeImage= new ImageIcon("Res/snake.png");
    static final  int screenWidth=600;
    static final  int screenHeight=600;
    static  final  int unit_Size=25;
    static  final int gameUnit= (screenWidth*screenHeight)/unit_Size;
    static final int delay=100;
    static   int hardModeDelay=100;
    final int[] x = new int[gameUnit];
    final int[] y = new int[gameUnit];

    int bodyParts=1;
    int appleEaten=0;
    int apple_X;
    int apple_Y;
    char direction ='R';
    boolean running= false;
    boolean menu=true;
    Timer timer;
    Timer timer2;
    Random random;
    boolean Pause = false;
    boolean gameOver=false;
    JButton normalButton= new JButton("Normal");
    JButton hardButton= new JButton("Hard");
    boolean unpause;
    int NM_HighScore;
    int HM_HighScore;

    GamePanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        audioInputStream= AudioSystem.getAudioInputStream(bgMusic);
        bgMusicClip= AudioSystem.getClip();
        bgMusicClip.open(audioInputStream);

        audioInputStream2=AudioSystem.getAudioInputStream(pickupFIle);
        pickupCLip=AudioSystem.getClip();
        pickupCLip.open(audioInputStream2);

        audioInputStream3= AudioSystem.getAudioInputStream(loseFile);
        loseClip=AudioSystem.getClip();
        loseClip.open(audioInputStream3);

        normalButton.setFocusable(false);
        normalButton.addActionListener(this);

        normalButton.setForeground(Color.red);
        normalButton.setBackground(Color.black.darker());
        normalButton.setOpaque(false);
        normalButton.setVisible(false);
        this.add(normalButton);
        hardButton.setFocusable(false);
        hardButton.addActionListener(this);

        hardButton.setForeground(Color.red);
        hardButton.setBackground(Color.black.darker());
        hardButton.setOpaque(false);
        hardButton.setVisible(false);
        this.add(hardButton);


        countdownLabel.setForeground(Color.blue.brighter());
        countdownLabel.setVisible(true);

        running=true;

        timer2=new Timer(hardModeDelay,this);
        timer=new Timer(delay,this);
        random =new Random();
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black.darker());
        this.setOpaque(true);
        this.setFocusable(true);
        this .addKeyListener(new MyKeyAdapter());



        apple_X= random.nextInt(screenWidth/unit_Size)*unit_Size;
        apple_Y= random.nextInt(screenHeight/unit_Size)*unit_Size;


        t = new Timer(1000, new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                Timer++;
                countdownLabel.setText(format(Timer / 60) + ":" + format(Timer % 60));


            }

        });
        countdownLabel.setPreferredSize(new Dimension(570,40));

        countdownLabel.setHorizontalAlignment(JLabel.RIGHT);
        countdownLabel.setVerticalAlignment(JLabel.TOP);

    }
    private static String format(int i) {
        String result = valueOf(i);
        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;

    }





    public void startGame() {


        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                x[i]=0;
                y[i]=0;
            }
        }


        timer.start();
        timer2.stop();

        appleEaten=0;
        bodyParts=1;

        newApple();
        running=true;


    }
    public void hardMode() throws IOException {

        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                x[i]=0;
                y[i]=0;
            }
        }
        if (Timer==1){

            hardModeDelay=10;
        }


        timer2.start();
        timer.stop();

        appleEaten=0;
        bodyParts=1;

        newApple();
        running=true;


    }



    public void startMenu(Graphics g)  {


        countdownLabel.setVisible(false);
        normalButton.setBounds(170, 300, 100, 70);
        hardButton.setBounds(350, screenHeight/2, 100, 70);
        normalButton.setVisible(true);
        hardButton.setVisible(true);


        g.setColor(new Color(0x07FFA2));
        g.setFont(new Font("Ink Free", Font.BOLD, 55));
        FontMetrics fontMetrics3 = getFontMetrics(g.getFont());

        g.drawString("Snake Game ", (screenWidth - fontMetrics3.stringWidth("Snake Game")) / 2,80);
        g.drawImage(snakeImage.getImage(), (screenWidth-fontMetrics3.stringWidth("Snake Game")/2), 10, null);


        g.setColor(Color.red.darker());
        g.setFont(new Font("Ink Free", Font.BOLD, 45));
        FontMetrics fontMetrics2 = getFontMetrics(g.getFont());

        g.drawString("Choose the difficulty" , (screenWidth - fontMetrics2.stringWidth("Choose the difficulty") ) / 2,200);


    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            draw(g);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }


    }

    public void HM_highestScore(Graphics g) throws IOException {
        if (timer2.isRunning()){
            BufferedReader br = new BufferedReader(new FileReader("Res/HM_LeaderBord.txt"));

            while ((HM_score = br.read()) != -1) {
                g.setColor(new Color(0x1B63AB));
                g.setFont(new Font("Bold", Font.ITALIC, 18));

                g.drawString("Hard mode highest score :"+HM_score,5, 20);
                if (HM_score>HM_HighScore){
                    HM_HighScore=HM_score;
                }

            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter("Res/HM_LeaderBord.txt"));
            bw.write(HM_HighScore);
            bw.close();

        }

    }
    public void NM_HighestScore(Graphics g) throws IOException {

        if (timer.isRunning()){
            BufferedReader br2 = new BufferedReader(new FileReader("Res/NM_LeaderBord.txt"));

            while ((NM_score = br2.read()) != -1) {

                g.setColor(new Color(0x1B63AB));

                g.setFont(new Font("Bold", Font.ITALIC, 19));
                g.drawString("Normal mode leader bord :"+NM_score,0, 19);

                if (NM_score>NM_HighScore){
                    NM_HighScore=NM_score;
                }

            }
            br2.close();

            BufferedWriter bw2 = new BufferedWriter(new FileWriter("Res/NM_LeaderBord.txt"));
            bw2.write( NM_HighScore);
            bw2.close();
        }




    }


    public void draw(Graphics g) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        HM_highestScore(g.create());
        NM_HighestScore(g.create());




        if (Pause) {
            pause(g.create());
        }




        if (!menu) {
            gameOver(g.create());
            normalButton.setVisible(false);
            hardButton.setVisible(false);
            if (running) {
                countdownLabel.setFont(new Font("", Font.PLAIN, 30));
                countdownLabel.setVisible(true);
                this.add(countdownLabel);


                t.start();

                for (int i = 0; i < screenHeight / unit_Size; i++) {
                    g.drawLine(i * unit_Size, 0, i * unit_Size, screenHeight);
                }
                for (int i = 0; i < screenWidth / unit_Size; i++) {

                    g.drawLine(0, i * unit_Size, screenWidth, i * unit_Size);
                }

                g.setColor(Color.red.darker());
                g.fillOval(apple_X, apple_Y, unit_Size, unit_Size);

                for (int i = 0; i < bodyParts; i++) {
                    if (i == 0) {
                        g.setColor(ColorUIResource.green);
                        g.fillRect(x[i], y[i], unit_Size, unit_Size);
                    } else {
//                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));

                        g.setColor(new Color(45, 180, 0));
                        g.fillRect(x[i], y[i], unit_Size, unit_Size);
                        repaint();


                    }

                }

                g.setColor(new Color(0x780000));
                g.setFont(new Font("Ink Free", Font.BOLD, 35));
                FontMetrics fontMetrics = getFontMetrics(g.getFont());

                g.drawString("Score: " + appleEaten, (screenWidth - fontMetrics.stringWidth("Score: ") + appleEaten) / 2, g.getFont().getSize());
            }

        }
        else{


            countdownLabel.setText("00:00");
            Timer=0;
            startMenu(g);}

    }


    public void move() {

        if (running) {
            for (int i = bodyParts; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }
            switch (direction) {
                case 'U':
                    y[0] = y[0] - unit_Size;
                    break;

                case 'D':
                    y[0] = y[0] + unit_Size;
                    break;

                case 'L':
                    x[0] = x[0] - unit_Size;
                    break;

                case 'R':
                    x[0] = x[0] + unit_Size;
                    break;


            }
        }
    }


    public void checkApple() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if ((x[0]==apple_X)&&y[0]==apple_Y){

            pickupCLip.start();
            pickupCLip.loop(1);
            appleEaten++;
            bodyParts++;
            newApple();
            if (timer2.isRunning()){
                if (HM_HighScore<appleEaten){
                    HM_HighScore = appleEaten;
                }
            }
            if (timer.isRunning()){
                if (NM_HighScore<appleEaten){
                    NM_HighScore = appleEaten;
                }
            }

        }


    }

    public void newApple(){
        apple_X= random.nextInt(screenWidth/unit_Size)*unit_Size;
        apple_Y= random.nextInt(screenHeight/unit_Size)*unit_Size;

    }

    public void checkCollisions() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

//        this checks if head collides with body
        for (int i =bodyParts;i>0;i--){
            if ((x[0]==x[i])&&(y[0]==y[i])){
                running=false;
            }
        }
// check if head touches left border
        if (x[0]<0 ){
            running=false;
        }
// check if head touches right border
        if (x[0]>=screenWidth){
            running=false;
        }

        if (y[0]<0){
            running=false;
        }


        if(y[0]>=screenHeight){
            running=false;
        }
        if (!running){
            timer.stop();
            timer2.stop();
        }
        if (!running){

            loseClip.start();
            loseClip.loop(1);
        }


    }

    public void gameOver(Graphics g) throws  IOException {



        if (!running&&!menu) {
            t.stop();
            gameOver=true;
            g.setColor(Color.red.darker());
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics fontMetrics = getFontMetrics(g.getFont());

            g.drawString("Game Over", (screenWidth - fontMetrics.stringWidth("Game Over ")) / 2, screenHeight / 2);


            g.setColor(Color.red.darker());
            g.setFont(new Font("Ink Free", Font.BOLD, 45));
            FontMetrics fontMetrics2 = getFontMetrics(g.getFont());

            g.drawString("Score: " + appleEaten, (screenWidth - fontMetrics2.stringWidth("Score: ") + appleEaten) / 2, g.getFont().getSize());




            g.setColor(new Color(0x024252));
            g.setFont(new Font("Bold",Font.ITALIC, 30));
            g.drawString("Press Enter to continue",0,450 );

            BufferedReader br2 = new BufferedReader(new FileReader("Res/NM_LeaderBord.txt"));

            while ((NM_score = br2.read()) != -1) {
                g.setFont(new Font("Bold", Font.ITALIC, 30));


                g.drawString("Normal mode highest score : "+ NM_score,0, 150);


            }
            BufferedReader br = new BufferedReader(new FileReader("Res/HM_LeaderBord.txt"));

            while ((HM_score = br.read()) != -1) {
                g.setFont(new Font("Bold", Font.ITALIC, 30));


                g.drawString("Hard mode highest score : "+ HM_score,0, 100);


            }

            timer.stop();
            timer2.stop();

        }



    }


    public void pause(Graphics g) {

        if (running&&!menu&&!gameOver) {
            t.stop();
            Pause = true;
            g.setColor(Color.white.darker());
            g.setFont(new Font("Ink Free", Font.BOLD, 45));
            FontMetrics fontMetrics4 = getFontMetrics(g.getFont());
            g.drawString("The game is paused" , (screenWidth - fontMetrics4.stringWidth("The game is paused"))/2, screenHeight/2);
            g.drawString("Press Enter to unpause", 0, 550);





            if (timer2.isRunning()&&!timer.isRunning()){
                timer2.stop();
                unpause=true;
            }
            if(timer.isRunning()&&!timer2.isRunning()){
                timer.stop();
                unpause=false;


            }

        }



    }
    public void unpause(){

        if(Pause||!menu||!gameOver){



            if (unpause) {

                timer2.start();

            }
            if (!unpause) {
                timer.start();
            }

        }

        Pause=false;

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (Timer==10){
            hardModeDelay=65;
            timer2.setDelay(hardModeDelay);
        }
        if (Timer==20){
            hardModeDelay=60;
            timer2.setDelay(hardModeDelay);
        }
        if (Timer==30){
            hardModeDelay=55;
            timer2.setDelay(hardModeDelay);
        }
        if (Timer==40){
            hardModeDelay=50;
            timer2.setDelay(hardModeDelay);
        }
        if (Timer==50){
            hardModeDelay=40;
            timer2.setDelay(hardModeDelay);
        }

        if (running){
            move();
            try {
                checkApple();
                checkCollisions();


            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                ex.printStackTrace();
            }




        }
        repaint();
        if ( e.getSource()==normalButton){
            menu=false;
                bgMusicClip.loop(1);


            if (!running&&gameOver){

                gameOver=false;
                startGame();


                try {
                    draw(getGraphics().create());
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                    ex.printStackTrace();
                }

                direction='R';


            }
            timer2.stop();
            timer.start();
        }

        if ( e.getSource()==hardButton){
            menu=false;

                bgMusicClip.loop(1);

            if (!running&&gameOver){

                gameOver=false;
                try {
                    hardMode();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


                try {
                    draw(getGraphics().create());
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                    ex.printStackTrace();
                }

                direction='R';


            }
            timer.stop();

            timer2.start();
        }


    }

    public class MyKeyAdapter extends KeyAdapter{


        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()){
                case  KeyEvent.VK_LEFT ,KeyEvent.VK_A:
                    if (timer.isRunning()||timer2.isRunning()) {

                        if (direction != 'R') {
                            direction = 'L';
                        }
                    }
                    break;

                case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                    if (timer.isRunning()|| timer2.isRunning()) {

                        if (direction != 'L') {
                            direction = 'R';
                        }
                    }
                    break;

                case KeyEvent.VK_UP,KeyEvent.VK_W:
                    if (timer.isRunning()||timer2.isRunning()) {

                        if (direction != 'D') {
                            direction = 'U';
                        }
                    }
                    break;

                case KeyEvent.VK_DOWN,KeyEvent.VK_S:

                    if (timer.isRunning()||timer2.isRunning()) {

                        if (direction != 'U') {
                            direction = 'D';
                        }


                    }
                    break;

                case KeyEvent.VK_ENTER:

                    System.out.println(Timer);
                    if (gameOver){
                        menu=true;
                        repaint();
                    }

                    if (Pause){
                        unpause();

                    }



                    break;

                case KeyEvent.VK_SPACE:


                    pause(getGraphics().create());
                    break;
                case KeyEvent.VK_I:

                    System.out.println("high Score "+HM_HighScore);
                    System.out.println("Normal Score "+ NM_HighScore);
                    System.out.println("Apple "+appleEaten);

                    break;

                case KeyEvent.VK_M:
                    if (bgMusicClip.isRunning()){
                        bgMusicClip.stop();
                    }
                    else  if(!bgMusicClip.isRunning()){
                        bgMusicClip.start();
                    }
                    break;


                case  KeyEvent.VK_ESCAPE:

                    System.exit(0);
                    break;




            }

        }
    }




}


