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
   * Creates a new picture object with a given image
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
   * returns an chunk of the image where x and y are the center and size is the radius
   * So it should be a square
   *
   * @param x
   * @param y
   * @param size
   * @return
   */
  public Image chunk(int x, int y, int size) {
    return image.getSubimage(x, y, size, size);
  }

  public BufferedImage get_image() {
    return image;
  }
}
