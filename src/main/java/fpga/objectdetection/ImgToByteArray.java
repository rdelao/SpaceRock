package fpga.objectdetection;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Rob on 2/25/2017.
 */

// look up java.util grayscale
// white space = debris.  black space = empty space

class ImgToByteArray {

    private final int INTENSITY_THRESHOLD = 10;

    private int imgSize;
    private File file;
    private BufferedImage image;
    private byte[][] pixelMap;


    ImgToByteArray(String s, int imgSize) throws IOException {
        this.imgSize = imgSize;
        image = ImageIO.read(ImgToByteArray.class.getResource(s));
        pixelMap =  new byte[imgSize][imgSize];
    }

    byte[][] parseMap()
    {
        for(int x = 0; x < imgSize; x++)
        {
            for(int y = 0; y < imgSize; y++)
            {
                readXYCoor(x,y);
            }
        }
        return pixelMap;
    }

    private void readXYCoor(int x, int y)
    {
        int clr =  image.getRGB(x,y);
        int blue = clr & 0x000000ff;
        int green = ( clr & 0x0000ff00 ) >> 16;
        int red = ( clr & 0x00ff0000 ) >> 32;

        int avgIntensity = (blue + red + green ) / 3;

        if ( avgIntensity > INTENSITY_THRESHOLD )
        {
            pixelMap[x][y] = 1;
        }
        else
        {
            pixelMap[x][y] = 0;
        }
    }

}