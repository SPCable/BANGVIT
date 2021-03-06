package duckhunt.GUI;

//import duckhunt.controller.DogController;
import duckhunt.controller.DuckController;
import duckhunt.model.Dog;
import duckhunt.model.Duck;
import duckhunt.utility.Resources.Resources;
import duckhunt.utility.sound.Sound;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import duckhunt.controller.GameListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Vittorio Polverino
 */
public class GamePanel extends JPanel implements MouseMotionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Cursor CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(), "null");
    private static final int BULLET_NUMBER = 10;

    private BufferedImage backgroundImg;
    private BufferedImage cursorImg;
    private BufferedImage dogCurrentImage;
    private BufferedImage duckCurrentImage;
    private BufferedImage flyAwayImage;
    private BufferedImage[] ammo;
    private BufferedImage gameResultImage;
    private BufferedImage pressEnterImage;

    private Sound shootSound;
    private Sound endOfGameSound;

    private Rectangle cursorRectangle;

    //private DogController dogController;
    private DuckController duckController;

    private GameListener gameListener;

    private Dog dog;
    private ArrayList<Duck> ducks = new ArrayList<Duck>();

    private GameThread gameThread;

    private boolean isGameFinished;
    private boolean showImage;

    private int currentAmmoNumber;
    private int killedDucks;

    public GamePanel() {
        initPanel();
        gameThread.start();
    }

    public void initPanel() {
        this.setLayout(null);
        this.setCursor(CURSOR);
        this.addMouseMotionListener(this);

        Resources.getSound("/sounds/gameIntro.wav").play();
        backgroundImg = Resources.getImage("/images/gameBackground.png");
        duckCurrentImage = Resources.getImage("/images/duckUpRight0.png");
        dogCurrentImage = Resources.getImage("/images/dogRight0.png");
        cursorImg = Resources.getImage("/images/gunsight.png");
        flyAwayImage = Resources.getImage("/images/flyAway.png");
        pressEnterImage = Resources.getImage("/images/pressEnter.png");
        shootSound = Resources.getSound("/sounds/shoot.wav");
        endOfGameSound = Resources.getSound("/sounds/gameClear.wav");
        ammo = new BufferedImage[10];
        for (int i = 0; i < BULLET_NUMBER; i++) {
            ammo[i] = Resources.getImage("/images/bullet.png");
        }
        showImage = true;
        isGameFinished = false;
        cursorRectangle = new Rectangle();
        cursorRectangle.setSize(cursorImg.getWidth(null), cursorImg.getHeight(null));
        cursorRectangle.setLocation(-cursorImg.getWidth(null), -cursorImg.getHeight(null));
        gameThread = new GameThread();

        this.addMouseListener(new MouseAdapter() {
            int dem = 0;
            @Override
            public void mousePressed(MouseEvent e) {
                shootSound.play();
                currentAmmoNumber--;
                Point hitPoint = e.getPoint();
                        // int x=e.getX();
                        // int y=e.getY();
                        // System.out.println(x+","+y);
                        // int q=ducks.get(1).getX();
                        // int w=ducks.get(1).getY();
                        // System.out.println(q+","+w);
                if (ducks.get(0) != null || ducks.get(1) != null || ducks.get(2) != null) { //       // 
                    duckController.decreaseAmmunition();        
                    // hitPoint.x -= ducks.get(0).getX();
                    // hitPoint.y -= ducks.get(0).getY();
                    
                    //contains(duckController.getCurrentImage(), hitPoint.x-10, hitPoint.y-10)
                    if (new Rectangle(ducks.get(0).getX()+40, ducks.get(0).getY(),47,100).contains(getMousePosition())
                        || new Rectangle(ducks.get(1).getX()+40, ducks.get(1).getY(),48,100).contains(getMousePosition())
                        || new Rectangle(ducks.get(2).getX()+40, ducks.get(2).getY(),46,100).contains(getMousePosition())
                        ) {
                        //
                        //    
                        // Random rand= new Random();
                        // int randomNum = rand.nextInt(10) + 1;
                           if (new Rectangle(ducks.get(0).getX()+40, ducks.get(0).getY(),47,100).contains(getMousePosition())){
                               //dem++;
                               System.out.println("Da bang trung 1" );
                            //    if(dem == 1)
                            //    {
                                    duckController.theDuckWasHit(true);
                                    killedDucks++;
                                    dem = 0;
                               //}
                            }
                            else if(new Rectangle(ducks.get(1).getX()+40, ducks.get(1).getY(),47,100).contains(getMousePosition()))
                            {
                                System.out.println("Da bang trung 2" );
                                duckController.theDuckWasHit1(true);
                                    killedDucks++;
                                    dem = 0;
                            }
                            else if(new Rectangle(ducks.get(2).getX()+40, ducks.get(2).getY(),47,100).contains(getMousePosition()))
                            {
                                System.out.println("Da bang trung 3" );
                                //duckController.theDuckWasHit(true);
                                    killedDucks++;
                                    dem = 0;
                            }
                    }
                }
                ///////////////////////////////////
                
            }
        });



        
    }

    public boolean contains(BufferedImage image, int x, int y) {
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
            return false;
        }
        Color pixel = new Color(image.getRGB(x, y), true);
        return pixel.getAlpha() > 128;
    }

    public void setDogCurrentImage(BufferedImage pImage) {
        this.dogCurrentImage = pImage;
    }

    public void setDuckCurrentImage(BufferedImage pImage) {
        this.duckCurrentImage = pImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2D.drawImage(backgroundImg, 0, 0, this);
        g2D.drawImage(cursorImg, this.cursorRectangle.x, this.cursorRectangle.y, this);

        // if (!dogController.isIntroAnimationFinished()) {
        //     g2D.drawImage(dogCurrentImage, dog.getX(), dog.getY(), this);
        // }

        for (int i = 0; i < currentAmmoNumber; i++) {
            g2D.drawImage(ammo[i], i * 30, 520, this);
        }

        if (duckController.isDuckVisible(1)) {
            g2D.drawImage(duckCurrentImage, ducks.get(1).getX(), ducks.get(1).getY(), this);
        }

        if (duckController.isDuckVisible(2)) {
            g2D.drawImage(duckCurrentImage, ducks.get(2).getX(), ducks.get(2).getY(), this);
        }

        if (duckController.isDuckVisible(0)) {
            g2D.drawImage(duckCurrentImage, ducks.get(0).getX(), ducks.get(0).getY(), this);
        }

        if (duckController.isFlownAway()) {
            g2D.drawImage(flyAwayImage, 300, 100, this);
        }
        

        if (isGameFinished) {
            g2D.drawImage(gameResultImage, 300, 100, this);
            if (showImage && pressEnterImage != null) {
                g2D.drawImage(pressEnterImage, 300, 150, this);
            }
        }

    }

    private void imageBlinker() {
        ActionListener listener = (ActionEvent e) -> {
            if (pressEnterImage != null) {
                showImage = !showImage;
                repaint();
            }
        };
        Timer timer = new Timer(600, listener);
        timer.start();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        cursorRectangle.x = e.getPoint().x - cursorRectangle.width / 2;
        cursorRectangle.y = e.getPoint().y - cursorRectangle.height / 2;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // 
    }

    public void addListener(GameListener pListener) {
        gameListener = pListener;
    }

    public void notifyGameStatus() {
        if (isGameFinished) {
            gameListener.gameIsFinished();
        }
    }

    public void addChim(int n)
    {
        for(int i = 0 ;i<n;i++)
        {
            ducks.add(new Duck());
        }
    }

    public class GameThread implements Runnable {

        static final int DUCK_NUMBER = 10;
        private Thread thread;
        private int i;

        public GameThread() {
            //dog = new Dog();
            //dogController = DogController.getIstance();
            duckController = DuckController.getIstance();
            //dogController.setDog(dog);
            //dogController.setPanel(GamePanel.this);
            duckController.setPanel(GamePanel.this);
        }

        public void start() {
            reset();
            addChim(3);
            thread = new Thread(this);
            thread.start();
        }

        public void stop() {
            duckController.theDuckIsFlownAway(false);
     
            currentAmmoNumber = 0;
            endOfGameSound.play();
            imageBlinker();
            notifyGameStatus();
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }

        private void reset() {
            isGameFinished = false;
            duckController.theDuckIsFlownAway(false);
    
            isGameFinished = false;
            killedDucks = 0;
            i = 0;
        }

        @Override
        public void run() {
            while (!isGameFinished) {
                try {
                    // dogController.getIntroAnimation().start();
                    // while (i < DUCK_NUMBER) {
                        currentAmmoNumber = 10;
                        duckController.setDuck(ducks.get(0));
                        duckController.setDuck(ducks.get(1));
                        duckController.setDuck(ducks.get(2));
                        duckController.getDuckAnimation(3);
                    //}
                    // if (killedDucks > 100) {
                    //     System.out.println("YOU WIN");
                    //     gameResultImage = Resources.getImage("/images/youWin.png");
                    //     repaint();
                    // } else {
                    //     System.out.println("YOU LOSE");
                    //     gameResultImage = Resources.getImage("/images/gameover.png");
                    //     repaint();
                    // }
                    // isGameFinished = true;
                } catch (Exception ex) {
                    System.out.println("an error occured during game thread execution");
                }
            }
            stop();
        }
    }
}


