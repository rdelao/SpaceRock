package sensor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Sean on 2/4/17.
 */
public class Picture {
  private static int nextid = 0;
  private BufferedImage image;

  private int id;

  /**
   * Creates a new picture object with a given imageView
   * @param image
   */
  public Picture(BufferedImage image) {
    this.image = image;
    this.id = nextid;
    nextid += 1;
  }

  public int getId() {
    return id;
  }

  /**
   * Returns an chunk of the imageView where x and y are the center and size is the width of a square
   *
   * @param x center horizontal
   * @param y center vertical
   * @param size width/height
   * @return bufferedImage of area
   */
  public BufferedImage chunk(int x, int y, int size) {
    int radius = size /2;
    return image.getSubimage(x-radius, y-radius, size, size);
  }

  public BufferedImage getImage() {
    return image;
  }
}
