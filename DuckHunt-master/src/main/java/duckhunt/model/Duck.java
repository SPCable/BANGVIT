package duckhunt.model;

import java.awt.Rectangle;
import java.util.Random;


/**
 * 
 * @author Vittorio Polverino
 */
public class Duck {

    private static final int START_Y_COORDINATE = 391;
    private final Rectangle rectangle;


    public Duck() {
        rectangle = new Rectangle(80, 80);
        rectangle.setLocation(randomX(),  START_Y_COORDINATE);
    }

    public int speed()
    {
        return this.randomSpeed();
    }

    public void setX(final int pX) {
        this.rectangle.x = pX;
    }

    public int getX() {
        return rectangle.x;
    }

    public void setY(final int pY) {
        this.rectangle.y = pY;
    }

    public int getY() {
        return rectangle.y;
    }

    private int randomX() {
        Random random = new Random();
        int randomValue = random.nextInt(789) + 20;
        return randomValue; 
    }
    private int randomSpeed()
    {
        Random random = new Random();
        int value1 = random.nextInt(9);
        if(value1!=0 && value1!=1)
        {
            return value1;
        }
        else
        {
            return 3;
        }
    }
}
