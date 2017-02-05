package Commands;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by magik on 2/5/2017.
 */
public class Incoming implements Serializable{
    Asteroid[] asteroids;
    Image[] images;
    public Incoming(Asteroid asteroid){
        this.asteroids=asteroids;
    }
    public Incoming(Image image){
        this.images=images;
    }
}
