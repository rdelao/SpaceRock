package Commands;

import java.io.Serializable;

/**
 @author David Ringo created: 2017.02.16 class:   cs351 project: spacerock-gui description: */
public interface FrameData extends Serializable {
    AsteroidData[] getAsteroids();

    long getTimestamp();


}
