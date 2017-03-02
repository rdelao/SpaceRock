package sensor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Sean on 2/4/17.
 */
public class Picture {
  private BufferedImage image;

  /**
   * Creates a new picture object with a given imageView
   * @param image
   */
  public Picture(BufferedImage image) {
    this.image = image;
  }

  /**
   * Returns an frame of the imageView where x and y are the center and size is the width of a square
   *
   * @param x center horizontal
   * @param y center vertical
   * @param size width/height
   * @return bufferedImage of window
   */
  public BufferedImage frame(int x, int y, int size) {
    int radius = size /2;
    return image.getSubimage(x-radius, y-radius, size, size);
  }

  public BufferedImage getImage() {
    return image;
  }
}
