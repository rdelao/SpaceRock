package Network;

import Commands.AsteroidData;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 Created by David R. 02/13/2017
 <p>
 This is class mocking the asteroids that the satellite might encounter.
 */
public class DummyAsteroid implements AsteroidData, Serializable {
    private final double size;
    private final long id;
    private double x;
    private double y;
    private Point2D veloc;


    /**
     @param loc Initial location (in pixels) of the DummyAsteroid
     @param veloc Velocity (in pixels per tick) of the DummyAsteroid
     @param size Size (in pixels) of the DummyAsteroid
     @param id a Unique ID for this DummyAsteroid
     */
    public DummyAsteroid(Point2D loc, Point2D veloc, double size, long id) {
        this.x = loc.getX();
        this.y = loc.getY();
        this.veloc = veloc;
        this.size = size;
        this.id = id;
    }


    public DummyAsteroid(DummyAsteroid orig) {
        this.id = orig.id;
        this.x = orig.x;
        this.y = orig.y;
        this.size = orig.size;
        this.veloc = orig.veloc;
    }


    @Override
    public String toString() {
        return String.format("ROCK{id: %d, size: %.3f, x: %.3f, y: %.3f}",
                             id, size, x, y);
    }


    public void step() {
        this.x += veloc.getX();
        this.y += veloc.getY();
    }


    public DummyAsteroid copy() {
        return new DummyAsteroid(this);
    }


    @Override
    public Point2D getLoc() {
        return new Point2D.Double(x, y);
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