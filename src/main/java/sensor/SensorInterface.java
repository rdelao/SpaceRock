package sensor;

import java.awt.image.BufferedImage;

/**
 *
 * Created by sean on 2/4/17.
 */
public interface SensorInterface {
  /**
   * Assesses and returns the ready of the SensorSimulation
   * @return true if camera is operational
   */
  public boolean ready();

  /**
   * Check imageView capture ready
   * @return true if capture of imageView is complete.
   */
  public boolean imageReady();

  /**
   * Notifies camera to take a picture.
   */
  public void takePicture();

  /**
   * Notifies the camera to set the zoom level
   * @param zoom Level of zoom (0-3)
   */
  public void setZoom(int zoom);

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
