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

    private int[] xDirection = new int[5];
    private int[] yDirection= new int[5];;
    private int[] x= new int[5];;
    private int[] y= new int[5];;


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
        for(int i = 0; i<5;i++)
        {
            xDirection[i] = RIGHT;
            yDirection[i] = DOWN;
            //spriteSheet[i] = new Spritesheet();
            x[i] = 1;
            y[i] = 1;
        }
        isDuckVisible = false;
        wasDuckHit = false;
        isDead = false;
        flownAway = false;
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
            for(int i = 0;i<5;i++)
            {
                x[i] = ducks.get(i).getX();
                y[i] = ducks.get(i).getY();
                x[i] += xDirection[i];
                y[i] += yDirection[i];
            }

            //System.out.println(xDirection1 +" "+ yDirection1);
            Thread.sleep(t);
        } catch (Exception e) {
        }
        for(int i = 0;i<5;i++)
        {
            ducks.get(i).setX(x[i]);
            ducks.get(i).setY(y[i]);

        if (ducks.get(i).getX() <= 0) {
            this.xDirection[i] = RIGHT;

        } else if (ducks.get(i).getX() >= 750) {
            this.xDirection[i] = LEFT;
        }
        if (ducks.get(i).getY() <= 0) {
            this.yDirection[i] = DOWN;
        } else if (ducks.get(i).getY() >= 390) {
            this.yDirection[i] = UP;
        }
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

    }

    private void flyAway(int id) {
        spriteSheet[1].setFrames(3, "duckFlyAway");
        spriteSheet[1].setDelay(DELAY);
        spriteSheet[1].update();
        currentImage[1] = spriteSheet[1].getCurrentFrame();


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