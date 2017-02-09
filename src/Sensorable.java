import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by sean on 2/4/17.
 */
public interface Sensorable {
  /**
   * Assesses and returns the status of the Camera
   * @return int representing the status of the camera ??
   */
  public int status();

  /**
   * Notifies camera to take a picture with a given zoom level (1-3) ??
   * @param zoom Level of zoom (1-3)
   * @return Generated id of the picture
   */
  public int takePicture(int zoom);

  /**
   * Notified camera to take a picture with no zooming.
   * @return Generated id of the picture
   */
  public int takePicture();

  /**
   * Generates an image at the given time with the given zoom level
   * @param id
   * @param x
   * @param y
   * @param size
   * @return Image which is just a subsection of the larger picture
   */
  public Image imageChunk(int id, int x, int y, int size);

  /**
   * Occasionally it is necessary to get the entire Picture object with a given id.
   * @param id
   * @return Picture object for the given id
   */
  public Picture getPicture(int id);
}
