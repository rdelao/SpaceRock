package Commands;

import Util.ImageUtil;

import java.awt.*;
import java.io.Serializable;


/**
 Created by David R.
 IncomingImage objects can be used as a serializable form of
 */
public class IncomingImage implements Serializable {
    public final int[] pixels;
    public final int height;
    public final int width;
    public final long id;


    /**
     Construct a new IncomingImage object.

     @param pixels Array of (greyscale) pixels
     @param height Width of image
     @param width Height of image
     */
    public IncomingImage(int[] pixels, int height, int width, long id) {
        if (height * width != pixels.length)
            throw new IllegalArgumentException("pixel array must be have a length equal to height" +
                                               " * width");
        this.pixels = pixels;
        this.height = height;
        this.width = width;
        this.id = id;
    }


    /**
     Construct an new IncomingImage given an Image.  Image will be converted to greyscale, if it
     isn't already.

     @param img Image basis
     */
    public IncomingImage(Image img, long id) {
        this(ImageUtil.greyscalePixels(img), img.getHeight(null), img.getWidth(null), id);
    }


    /** @return an Image formed from the underlying pixel array */
    public Image getImage() {
        return ImageUtil.pixelsToImage(pixels, height, width);
    }
}
