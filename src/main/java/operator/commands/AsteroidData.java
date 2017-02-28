package operator.commands;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 AsteroidData is an interface for accessing the minimal data required by the operator software to
 model objects in the the view of SpaceRock camera.
 */
public interface AsteroidData extends Serializable {

    /**
     @return the {@code Point2D} location (likely, centerpoint) of this asteroid within the view of
     the SpaceRock system.
     */
    Point2D getLoc();

    /**
     @return the size (likely, diameter) of this asteroid
     */
    double getSize();

    /**
     @return the Satellite-assigned ID of this asteroid
     */
    long getID();
}
