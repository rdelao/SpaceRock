package sensor;

import java.awt.image.BufferedImage;

/**
 *
 * Created by sean on 2/4/17.
 */
public interface SensorInterface {
  /**
   * Assesses the camera and returns true if the camera is on and operational
   * @return true if camera is operational
   */
  public boolean ready();

  /**
   * Check if image is ready to be grabbed and chunked.
   * @return true if the image is ready
   */
  public boolean imageReady();

  /**
   * Notifies camera to take a picture. This will start a thread for taking a picture. To determine
   * if new picture is ready, use imageReady().
   */
  public void takePicture();

  /**
   * Notifies the camera to set the zoom level, using one of the predefined levels defined by the
   * ZoomLevel class.
   * @param zoom ZoomLevel enum
   */
  public void setZoom(ZoomLevel zoom);

  /**
   * Return square window of the image.
   * @param x horizontal center of window
   * @param y vertical center of window
   * @param size width/height of window
   * @return image frame using the defined (x,y) and size
   */
  public BufferedImage getFrame(int x, int y, int size);

  /**
   * Attempt to turn camera on.
   */
  void on();

  /**
   * Attempt to turn camera off.
   */
  void off();

  /**
   * Reset camera
   */
  void reset();

}
