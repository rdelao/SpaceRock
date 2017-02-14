package Commands;

import java.io.Serializable;

/**
 Created by magik on 2/5/2017.
 Modified by David R., 2/12/2017
 <p>
 An IncomingData object represents the asteroid data associate with a single image taken by the
 satellite camera.
 */
public class IncomingData implements Serializable {

    public final AsteroidData[] asteroids;
    public final long timestamp;


    public IncomingData(AsteroidData[] asteroids, long timestamp) {
        this.asteroids = asteroids;
        this.timestamp = timestamp;
    }
}
