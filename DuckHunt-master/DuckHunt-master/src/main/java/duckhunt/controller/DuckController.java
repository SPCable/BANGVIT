package duckhunt.controller;

import duckhunt.GUI.GamePanel;
import duckhunt.model.Duck;
import duckhunt.utility.Resources.Resources;
import duckhunt.utility.sound.Sound;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

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

    private static DuckController duckControllerIstance = null;
    private final Spritesheet spriteSheet;
    private BufferedImage currentImage;
    private final DuckAnimation duckAnimation;
    private GamePanel panel;
    private Duck duck;

    private int xDirection;
    private int yDirection;
    private int x;
    private int y;
    private int ammunition;

    private final Sound deadSound;
    private final Sound duckCall;

    private boolean isDuckVisible;
    private boolean wasDuckHit;
    private boolean isDead;
    private boolean flownAway;

    public int ss = 0;
    Timer timer = new Timer();
    TimerTask task = new TimerTask(){
    
        @Override
        public void run() {
            ss++;
            System.out.println(ss);
        }
    };



    private DuckController() {
        currentImage = Resources.getImage("/images/duckUpRight0.png");
        deadSound = Resources.getSound("/sounds/duckDead.wav");
        duckCall = Resources.getSound("/sounds/duckCall.wav");
        spriteSheet = new Spritesheet();
        duckAnimation = new DuckAnimation();
        xDirection = RIGHT;
        yDirection = DOWN;
        isDuckVisible = false;
        wasDuckHit = false;
        isDead = false;
        flownAway = false;
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

    public void setDuck(final Duck pDuck) {
        this.duck = pDuck;
    }

    public Duck getDuck() {
        return duck;
    }

    private void flight() {
        if (xDirection == RIGHT && yDirection == UP) {
            spriteSheet.setFrames(2, "duckUpRight");
            spriteSheet.setDelay(DELAY);
            spriteSheet.update();
            currentImage = spriteSheet.getCurrentFrame();
        } else if (xDirection == RIGHT && yDirection == DOWN) {
            spriteSheet.setFrames(2, "duckRight");
            spriteSheet.setDelay(DELAY);
            spriteSheet.update();
            currentImage = spriteSheet.getCurrentFrame();
        } else if (xDirection == LEFT && yDirection == UP) {
            spriteSheet.setFrames(2, "duckUpLeft");
            spriteSheet.setDelay(DELAY);
            spriteSheet.update();
            currentImage = spriteSheet.getCurrentFrame();
        } else {
            spriteSheet.setFrames(2, "duckLeft");
            spriteSheet.setDelay(DELAY);
            spriteSheet.update();
            currentImage = spriteSheet.getCurrentFrame();
        }
        try {
            x = duck.getX();
            y = duck.getY();
            x += xDirection;
            y += yDirection;
            Thread.sleep(t);
        } catch (Exception e) {
        }
        duck.setX(x);
        duck.setY(y);

        if (duck.getX() <= 0) {
            this.xDirection = RIGHT;

        } else if (duck.getX() >= 750) {
            this.xDirection = LEFT;
        }
        if (duck.getY() <= 0) {
            this.yDirection = DOWN;
        } else if (duck.getY() >= 390) {
            this.yDirection = UP;
        }
    }

    public int t;

    private void dead() {
        deadSound.play();
        t = duck.speed();
        currentImage = Resources.getImage("/images/duckDead.png");
    }

    private void precipitate() {
        spriteSheet.setFrames(4, "duckPrecipitate");
        spriteSheet.setDelay(DELAY);
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();

        y = duck.getY();
        y += 6;
        duck.setY(y);
    }

    private void flyAway() {
        spriteSheet.setFrames(3, "duckFlyAway");
        spriteSheet.setDelay(DELAY);
        spriteSheet.update();
        currentImage = spriteSheet.getCurrentFrame();
        y = duck.getY();
        y -= 2;
        duck.setY(y);
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public boolean isDuckVisible() {
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
    }

    public DuckAnimation getDuckAnimation() {
        return duckAnimation;
    }

    public class DuckAnimation implements Runnable {
        private Thread thread;
        
        public void start() throws InterruptedException {
            reset();
            thread = new Thread(this);
            thread.start();
            t = duck.speed();
            thread.join();
        }

        public void stop() {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }

        private void reset() {
            isDuckVisible = true;
            wasDuckHit = false;
            isDead = false;
            flownAway = false;
            ammunition = 3;
            
        }
        public void run() {

            duckCall.play();
            while (!wasDuckHit && ammunition > 0) {
                flight();
                panel.setDuckCurrentImage(currentImage);
                panel.repaint();
            }
            if (ammunition == 0 ) {
                flownAway = true;
                while (duck.getY() > -50) {
                    flyAway();
                    panel.setDuckCurrentImage(currentImage);
                    panel.repaint();
                }
            } else {
                try {
                    dead();
                    panel.setDuckCurrentImage(currentImage);
                    panel.repaint();
                    Thread.sleep(500);
                    while (duck.getY() < 420) {
                        precipitate();
                        panel.setDuckCurrentImage(currentImage);
                        panel.repaint();
                    }
                    isDead = true;
                } catch (final InterruptedException ex) {
                    System.out.println("an error occured during duck animation thread");
                    stop();
                }
            }
            isDuckVisible = false;
            stop();
        }
    }
}