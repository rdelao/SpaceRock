import java.awt.*;

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
  public int take_picture(int zoom);

  /**
   * Notified camera to take a picture with no zooming.
   * @return Generated id of the picture
   */
  public int take_picture();

  /**
   * Generates an image at the given time with the given zoom level
   * @param id
   * @param x
   * @param y
   * @param size
   * @return Image which is just a subsection of the larger picture
   */
  public Image image_chunk(int id, int x, int y, int size);
}
