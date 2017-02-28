package fpga;

import java.awt.image.Kernel;
import java.awt.image.*;

/**
 * Created by Ally on 2/25/17.
 */
public class GaussianFilter
{

  public static BufferedImage blur( BufferedImage image) {
    int blurScale = 30;
    BufferedImage destination = null;

    destination = new BufferedImage( image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_BYTE_GRAY );


    float[] data = new float[blurScale * blurScale];
    float value = 1.0f / ( blurScale * blurScale );
    for ( int i = 0; i < data.length; i++ ) {
      data[i] = value;
    }
    Kernel kernel = new Kernel( blurScale, blurScale, data );
    ConvolveOp convolve = new ConvolveOp( kernel, ConvolveOp.EDGE_NO_OP, null );
    convolve.filter( image, destination );

    return destination;
  }

}
