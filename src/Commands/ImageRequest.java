package Commands;

import java.io.Serializable;

/**
 created by David R.
 <p>
 A data object describing a request for an image chunk from the satellite.
 */
public class ImageRequest implements Serializable {

    /** Satellite-side ID of the asteroid in question */
    public final long id;


    /**
     Create a request for the image chunk in which an Asteroid was found

     @param id ID of the Asteroid
     */
    public ImageRequest(long id) {
        this.id = id;
    }
}
