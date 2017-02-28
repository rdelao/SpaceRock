package operator.commands;

import operator.util.ImageUtil;

import java.awt.*;


/**
 Created by David R.
 ImageData objects can be used as a serializable form of image chunks captured by the satellite.
 */
public class ImageData implements ImageResponse {
    private final int[] pixels;
    private final int xOffset;
    private final int yOffset;
    private final int height;
    private final int width;
    private final long asteroidID;


    /**
     Construct a new ImageData object.

     @param pixels Array of (greyscale) pixels
     @param yOffset the y offset in the original image capture of this image chunk
     @param xOffset the x offset in the original image capture of this image chunk
     @param height Width of image
     @param width Height of image
     @param asteroidID ID of the Asteroid corresponding to the image
     */
    public ImageData(int[] pixels, int xOffset, int yOffset, int height, int width,
                     long asteroidID) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        if (height * width != pixels.length)
            throw new IllegalArgumentException("pixel array must be have a length equal to height" +
                                               " * width");
        this.pixels = pixels;
        this.height = height;
        this.width = width;
        this.asteroidID = asteroidID;
    }


    /**
     Construct an new ImageData given an Image (chunk).  Image will be converted to greyscale,
     if it isn't already.

     @param img Image basis
     @param asteroidID ID of the Asteroid corresponding to the image
     @param yOffset the y offset in the original image capture of this image chunk
     @param xOffset the x offset in the original image capture of this image chunk
     */
    public ImageData(Image img, long asteroidID, int xOffset, int yOffset) {
        this(ImageUtil.greyscalePixels(img), xOffset, yOffset,
             img.getHeight(null), img.getWidth(null), asteroidID);
    }


    /** @return the X offset of this image chunk within the original image capture */
    @Override
    public int getXOffset() {
        return xOffset;
    }


    /** @return the Y offset of this image chunk within the original image capture */
    @Override
    public int getYOffset() {
        return yOffset;
    }


    /**
     @return pixel array of this image.  Use ImageUtil to convert to a renderable image.
     */
    @Override
    public int[] getPixels() {
        return pixels;
    }


    /** @return the Height of the image */
    @Override
    public int getHeight() {
        return height;
    }


    /** @return the width of the image */
    @Override
    public int getWidth() {
        return width;
    }


    /** @return the ID of the asteroid corresponding to this image */
    @Override
    public long getAsteroidID() {
        return asteroidID;
    }


    /** @return an Image formed from the underlying pixel array */
    @Override
    public Image getImage() {
        return ImageUtil.pixelsToImage(pixels, height, width);
    }
}
