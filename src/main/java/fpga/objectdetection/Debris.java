package fpga.objectdetection;
/**
 * Created by Rob on 2/25/2017.
 */
public class Debris {

    int centerXLocation, centerYLocation;
    int frameX, frameY;
    int size;
    // the image or frame that this debris was found in
    int centerX, centerY;
    int diameter;

    Debris(int x, int y, int dia, int size)
    {
        centerX = x;
        centerY = y;
        diameter = dia;
        this.size = size;
    }
}
