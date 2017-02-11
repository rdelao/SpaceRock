package sensor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 * Created by sean on 2/4/17.
 */
public interface SensorInterface {
  /**
   * Assesses and returns the status of the SensorSimulation
   * @return true if camera is operational
   */
  public boolean status();

  /**
   * Check imageView capture status
   * @return true if capture of imageView is complete.
   */
  public boolean captureStatus();

  /**
   * Notifies camera to take a picture with a given zoom level (1-3) ??
   * @param zoom Level of zoom (1-3)
   */
  public void takePicture(int zoom);

  /**
   * Notified camera to take a picture with no zooming.
   */
  public void takePicture();

  /**
   * Return square window from the imageView.
   * @param x horizontal center of window
   * @param y vertical center of window
   * @param size width/height of window
   * @return windowed imageView from full imageView.
   */
  public BufferedImage getImageChunk(int x, int y, int size);

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
