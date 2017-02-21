package Commands;

/**
 Created by magik on 2/5/2017.
 Modified by David R., 2/12/2017
 <p>
 An AsteroidFrame object represents the asteroid data associate with a single image taken by the
 satellite camera.
 */
public class AsteroidFrame implements FrameData {

    public final AsteroidData[] asteroids;
    public final long timestamp;


    public AsteroidFrame(AsteroidData[] asteroids, long timestamp) {
        this.asteroids = asteroids;
        this.timestamp = timestamp;
    }


    @Override
    public AsteroidData[] getAsteroids() {
        return asteroids;
    }


    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
