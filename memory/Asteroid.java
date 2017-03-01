package memory;

/**
 * Created by Ethan on 2/28/2017.
 * Contains all data  passed from FPGA
 */
public class Asteroid {
    int centerXLocation, centerYLocation;
    int frameX, frameY;
    int size;
    // the image or frame that this debris was found in

    Asteroid(int x, int y, int size)
    {
        frameX = x;
        frameY = y;
        this.size = size;
    }

}

