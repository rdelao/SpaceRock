package Commands;

import java.io.Serializable;

/**
 created by David R.
 <p>
 A data object describing a request for an image chunk from the satellite.
 */
public class OutgoingImageRequest implements Serializable {

    /** Satellite-side id of the asteroid in question */
    public final long id;


    /**
     Create a request for the image chunk in which an Asteroid was found

     @param id id of the Asteroid
     */
    public OutgoingImageRequest(long id) {
        this.id = id;
    }
}
