package fpga.objectdetection;
/**
 * Created by Rob on 2/25/2017.
 */
public class Debris {

    int centerXLocation, centerYLocation;
    int frameX, frameY;
    int size;
    // the image or frame that this debris was found in

    public Debris(int x, int y, int size)
    {
        frameX = x;
        frameY = y;
        this.size = size;
    }
}
