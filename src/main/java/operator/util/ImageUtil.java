package operator.util; /**
 Created by David R.
 <p>
 The util.ImageUtil class holds several static methods for converting images between Image objects
 and integer array representations (in greyscale).  This is useful for (de)serialization since non of
 the standard Image classes are serializable.
 */


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ImageUtil {

    /**
     Convert an image to greyscale and return the array of integers representing its pixels using
     a default Averaging mode of desaturation.
     @param img Image to convert
     @return int array of pixels
     */
    public static int[] greyscalePixels(Image img) {
        return greyscalePixels(img, DesaturationMode.Average);
    }


    /**
     Convert an Image to an array of integers representing the pixels of a greyscale conversion.
     The type of Desaturation may be specified
     @param img  Image to convert
     @param mode DesaturationMode desired
     @return an int array of pixel values
     */
    public static int[] greyscalePixels(Image img, DesaturationMode mode) {
        BufferedImage bimg;
        if (img instanceof BufferedImage) {
            bimg = (BufferedImage) img;
        } else {
            bimg = new BufferedImage(img.getWidth(null),
                                     img.getHeight(null),
                                     BufferedImage.TYPE_BYTE_GRAY);
            Graphics g = bimg.getGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
        }
        return greyscalePixels(bimg, mode);
    }


    /**
     Convert a BufferedImage to greyscale pixels
     @param img  BufferedImage to convert
     @param mode Desaturation mode.
     @return an int array of pixel values
     */
    public static int[] greyscalePixels(BufferedImage img, DesaturationMode mode) {

        int[] pixArray = img.getRGB(0, 0, img.getWidth(), img.getHeight(),
                                    null, 0, img.getWidth());

        /* If it's already greyscale, we can just return the array */
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            return pixArray;
        }

        /* Else, use the desaturation mode to convert it */
        IntStream pixels = Arrays.stream(pixArray).parallel();
        switch (mode) {
            case Average:
                return pixels.map(
                        i -> desaturateByAverage((i & 0xFF0000) >> 16,
                                                 (i & 0x00FF00) >> 8,
                                                 (i & 0x0000FF))).toArray();
            case Lightness:
                return pixels.map(
                        i -> desaturateByLightness((i & 0xFF0000) >> 16,
                                                   (i & 0x00FF00) >> 8,
                                                   (i & 0x0000FF))).toArray();
            case Luminosity:
                return pixels.map(
                        i -> desaturateByLuminosity((i & 0xFF0000) >> 16,
                                                    (i & 0x00FF00) >> 8,
                                                    (i & 0x0000FF))).toArray();
            default:
                /* unreachable.  Compiler should know this... -__- */
                return null;
        }
    }


    public static int desaturateByLightness(int r, int g, int b) {
        if (r > b) {
            return (r + b > g ? b : g) / 2;
        } else {
            return (b + r > g ? r : g) / 2;
        }
    }


    public static int desaturateByLuminosity(int r, int g, int b) {
        return (int) (0.21 * r + 0.72 * g + 0.07 * b);
    }


    public static int desaturateByAverage(int r, int g, int b) {
        return (r + b + g) / 3;
    }


    /**
     Given an array of greyscale pixels (as ints) and image size params, return an Image for
     rendering.
     @param pixels Array of greyscale pixels.
     @param height Image height
     @param width  Image width
     @return a renderable Image (as used by a Graphics object, e.g.)
     */
    public static Image pixelsToImage(int[] pixels, int height, int width) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        img.setRGB(0, 0, width, height, pixels, 0, width);
        return img;
    }


    /**
     Enum describing three approaches for converting an image to greyscale.
     See https://docs.gimp.org/2.6/en/gimp-tool-desaturate.html
     */
    public enum DesaturationMode {
        Luminosity, Lightness, Average;
    }
}
