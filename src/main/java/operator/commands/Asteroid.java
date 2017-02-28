package operator.commands;

import java.awt.geom.Point2D;
import java.time.Instant;

/**
 * Created by magik on 2/5/2017. Modified by David R., 2/12/2017
 * <p>
 * The Asteroid class describes an immutable data object that _should_
 * be roughly isomorphic to what will be sent by the SpaceRock satellite code.
 * It implements {@code AsteroidData} which should help interface between
 * disparate code bases.
 */
public class Asteroid implements AsteroidData {

    public final Point2D location;
    public final double size;
    public long id;
    public Instant timestamp;


    public Asteroid(Point2D location, long id, double size, Instant timestamp) {
        this.location = location;
        this.id = id;
        this.size = size;
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return String.format("Asteroid ID=%d at (%.3f, %.3f)",
                             id, location.getX(), location.getY());
    }


    @Override
    public Point2D getLoc() {
        return location;
    }


    @Override
    public double getSize() {
        return size;
    }


    @Override
    public long getID() {
        return id;
    }
}
