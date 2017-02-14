package Commands;

import java.awt.*;
import java.util.Arrays;

/**
 IncomingListeners must implement two methods for handling incoming data from the SpaceRock
 satellite: either Image data or Asteroid Data.
 created by David R. 02/12/2017
 */
public interface IncomingListener {

    IncomingListener simplePrinter = new IncomingListener() {
        @Override
        public void newAsteroidData(AsteroidData[] asteroids, long timestamp) {
            System.out.printf("Incoming Asteroids [%d]:%n%s%n",
                              timestamp, Arrays.toString(asteroids));
        }


        @Override
        public void newImageData(Image img, long id) {
            System.out.printf("Incoming image for asteroid %d%n", id);
        }
    };


    /**
     @param asteroids The most recently detected asteroids
     @param timestamp The time when the image corresponding to the asteroids was taken
     */
    void newAsteroidData(AsteroidData[] asteroids, long timestamp);


    /**
     @param img An image chunk requested by the operator
     @param id The id of the object whose image chunk was requested
     */
    void newImageData(Image img, long id);

}
