package Commands;

import java.awt.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by magik on 2/5/2017.
 */
public class Incoming implements Serializable{
    Asteroid[] asteroids;
    long timestamp;
    public Incoming(Asteroid asteroid, long timestamp){
        this.asteroids=asteroids;
        this.timestamp = timestamp;
    }
}
