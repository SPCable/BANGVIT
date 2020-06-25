package duckhunt.controller;

import duckhunt.GUI.GamePanel;
import duckhunt.model.Duck;
import duckhunt.utility.Resources.Resources;
import duckhunt.utility.sound.Sound;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import duckhunt.controller.Spritesheet;

/**
 *
 * @author Vittorio Polverino
 */
public class DuckController {

    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    private static final int UP = -1;
    private static final int DOWN = 1;
    private static final int DELAY = 200;

    private static final int LEFT1 = -1;
    private static final int RIGHT1 = 1;
    private static final int UP1 = -1;
    private static final int DOWN1 = 1;
    private static final int DELAY1 = 200;

    private static DuckController duckControllerIstance = null;
    private final Spritesheet[] spriteSheet = new Spritesheet[5];
    private BufferedImage[] currentImage = new BufferedImage[5];
    private GamePanel panel;
    private ArrayList<Duck> ducks = new ArrayList<Duck>();

    private int xDirection;
    private int yDirection;
    private int x;
    private int y;

    
    private int xDirection1;
    private int yDirection1;
    private int x1;
    private int y1;

    private int ammunition;

    private final Sound deadSound;
    private final Sound duckCall;

    private boolean isDuckVisible;
    private boolean wasDuckHit;
    private boolean isDead;
    private boolean flownAway;
    public static int score = 0;

    public int ss = 0;
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            ss++;
            System.out.println(ss);
        }
    };

    private DuckController() {
        currentImage[1] = Resources.getImage("/images/duckUpRight0.png");
        currentImage[2] = Resources.getImage("/images/duckUpRight0.png");
        deadSound = Resources.getSound("/sounds/duckDead.wav");
        duckCall = Resources.getSound("/sounds/duckCall.wav");
        xDirection = RIGHT;
        yDirection = DOWN;
        spriteSheet[1] = new Spritesheet(1);
        xDirection1 = LEFT1;
        yDirection1 = DOWN1;
        isDuckVisible = false;
        wasDuckHit = false;
        isDead = false;
        flownAway = false;
        x1 = 1;
        y1 = 1;

        x = 1;
        y = 1;

    }

    public static DuckController getIstance() {
        if (duckControllerIstance == null) {
            return duckControllerIstance = new DuckController();
        }
        return duckControllerIstance;
    }

    public void setPanel(final GamePanel pPanel) {
        this.panel = pPanel;
    }

    public void setDuck(Duck ducks) {
        this.ducks.add(ducks);
    }

    public Duck getDuck(int id) {
        return ducks.get(id);
    }


    private void flight() {
        
        // } else if (xDirection == LEFT && yDirection == UP) {
        //     spriteSheet[1].setFrames(2, "duckUpLeft");
        //     spriteSheet[1].setDelay(DELAY);
        //     spriteSheet[1].update();
        //     currentImage[1] = spriteSheet[1].getCurrentFrame();
        // } else {
        //     spriteSheet[1].setFrames(2, "duckLeft");
        //     spriteSheet[1].setDelay(DELAY);
        //     spriteSheet[1].update();
        //     currentImage[1] = spriteSheet[1].getCurrentFrame();
        // }

        try {
            x = ducks.get(1).getX();
            y = ducks.get(1).getY();
            x += xDirection;
            y += yDirection;


            x1 = ducks.get(2).getX();
            y1 = ducks.get(2).getY();
            x1 += xDirection1;
            y1 += yDirection1;

            System.out.println(xDirection1 +" "+ yDirection1);
            Thread.sleep(t);
        } catch (Exception e) {
        }
        ducks.get(1).setX(x);
        ducks.get(1).setY(y);

        ducks.get(2).setX(x1);
        ducks.get(2).setY(y1);


        if (ducks.get(1).getX() <= 0) {
            this.xDirection = RIGHT;

        } else if (ducks.get(1).getX() >= 750) {
            this.xDirection = LEFT;
        }
        if (ducks.get(1).getY() <= 0) {
            this.yDirection = DOWN;
        } else if (ducks.get(1).getY() >= 390) {
            this.yDirection = UP;
        }

        if (ducks.get(2).getX() <= 0) {
            this.xDirection1 = RIGHT1;

        } else if (ducks.get(2).getX() >= 750) {
            this.xDirection1 = LEFT1;
        }
        if (ducks.get(2).getY() <= 0) {
            this.yDirection1 = DOWN1;
        } else if (ducks.get(2).getY() >= 390) {
            this.yDirection1 = UP1;
        }
    }

    public int t;

    public int dead(int id) {
        deadSound.play();
        t = ducks.get(id).speed();
        currentImage[1] = Resources.getImage("/images/duckDead.png");
        score = score + 100;
        return score;
    }

    private void precipitate(int id) {
        spriteSheet[1].setFrames(4, "duckPrecipitate");
        spriteSheet[1].setDelay(DELAY);
        spriteSheet[1].update();
        currentImage[1] = spriteSheet[1].getCurrentFrame();

        y = ducks.get(1).getY();
        y += 6;
        ducks.get(1).setY(y);

        y1 = ducks.get(2).getY();
        y1 += 6;
        ducks.get(2).setY(y1);
    }

    private void flyAway(int id) {
        spriteSheet[1].setFrames(3, "duckFlyAway");
        spriteSheet[1].setDelay(DELAY);
        spriteSheet[1].update();
        currentImage[1] = spriteSheet[1].getCurrentFrame();


        y = ducks.get(1).getY();
        y -= 2;
        score = 0;
        ducks.get(1).setY(y);

    }

    public BufferedImage getCurrentImage() {
        return currentImage[1];
    }

    public boolean isDuckVisible(int id) {
        return isDuckVisible;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isFlownAway() {
        return flownAway;
    }

    public void theDuckIsFlownAway(final boolean pValue) {
        this.flownAway = pValue;
    }

    public void theDuckWasHit(final boolean pValue) {
        this.wasDuckHit = pValue;
    }

    public void decreaseAmmunition() {
        ammunition--;
        System.out.println(ammunition);
    }

    public void  getDuckAnimation(int n) 
    {
        for(int i = 0; i<n;i++)
        {
            reset();
            start(i);
            run(i);
        }

    }
    public void start(int id)
    {
        reset();
        t = ducks.get(1).speed();
    }

    private void reset() {
        isDuckVisible = true;
        wasDuckHit = false;
        isDead = false;
        flownAway = false;
        ammunition = 10;

    }

    public void run(int id) {

        duckCall.play();
        while (!wasDuckHit && ammunition >0) {
            flight();
            panel.setDuckCurrentImage(currentImage[1]);
            panel.repaint();
        }
        if(wasDuckHit && ammunition >=0) {
            try {
                dead(id);
                panel.setDuckCurrentImage(currentImage[1]);
                panel.repaint();
                Thread.sleep(500);
                while (ducks.get(0).getY() < 420) {
                    precipitate(0);
                    panel.setDuckCurrentImage(currentImage[1]);
                    panel.repaint();
                }
                isDead = true;
            } catch (final InterruptedException ex) {
                System.out.println("an error occured during duck animation thread");
            }
        }
        else
        {
            if (ammunition == 0 ) {
                flownAway = true;
                while (ducks.get(1).getY() > -50) {
                    flyAway(1);
                    panel.setDuckCurrentImage(currentImage[1]);
                    panel.repaint();
                }
            } 
        }
        isDuckVisible = false;
    }
}