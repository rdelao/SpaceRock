package sensor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sean on 2/4/17.
 */
public class SensorSimulation implements SensorInterface {

  static final int[] image_size = {4000,4000};
  static final int MAX_ASTEROIDS = 5;
  private static final int TIME_STEP = 30;
  private Picture image;
  private int elapsed_seconds = 0;
  ArrayList<Asteroid> asteroids = new ArrayList<>();

  private Boolean status = false;

  private Boolean captureStatus = false;

  private boolean autoAdvance = true;
  private ZoomLevel zoom_level = ZoomLevel.NONE;

  /**
   * Initialize a camera object
   */
  public SensorSimulation() {

  }

  /**
   * For testing only
   * @param autoAdvance true to increment with takePicture
   */
  public SensorSimulation(boolean autoAdvance){
    this.autoAdvance = autoAdvance;
  }

  /**
   * Returns the ready of the SensorSimulation.
   * @return true if camera is operational
   */
  public boolean ready() {
    return status;
  }

  @Override
  public boolean imageReady() {
    return captureStatus;
  }

  @Override
  public void takePicture() {
    if (!status) {
      return;
    }
    Thread thread = new Thread() {
      @Override
      public void run() {
        synchronized (captureStatus) {
          if(autoAdvance){
            elapsed_seconds += TIME_STEP;
          }

          // Take a new picture
          System.out.println("Taking new picture at zoom level: " + zoom_level);
          image = (new ImageGenerator(elapsed_seconds, asteroids)).generateImage(elapsed_seconds, zoom_level);
          captureStatus = true;
        }
      }
    };
    captureStatus = false;
    thread.start();

  }

  @Override
  public void setZoom(ZoomLevel zoom) {
    this.zoom_level = zoom;
  }

  @Override
  public BufferedImage getImageChunk(int x, int y, int size) {
    if(captureStatus)
      return image.chunk(x, y, size);
    else return null;
  }

  @Override
  public void on() {
    Thread thread = new Thread() {
      @Override
      public void run() {
        synchronized (status) {
          System.out.println("Turning Sensor on...");
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          //Empty asteroids as image resets when turned off.
          asteroids.clear();
          status = true;
          captureStatus = false;
          System.out.println("Sensor on");
        }
      }
    };
    thread.start();
  }

  @Override
  public void off() {
    Thread thread = new Thread() {
      @Override
      public void run() {
        synchronized (status) {

          System.out.println("Turning sensor off...");

          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          status = false;
          image = null;
          captureStatus = false;
          System.out.println("Sensor off");
        }
      }
    };
    thread.start();
  }

  @Override
  public void reset() {
    Thread thread = new Thread() {
      @Override
      public void run() {
        synchronized (status) {
          System.out.println("Resetting Sensor...");
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          status = false;
          image = null;
          captureStatus = false;
          System.out.println("Sensor off");


          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          //Empty asteroids as image resets when turned off.
          asteroids.clear();
          status = true;
          captureStatus = false;
          System.out.println("Sensor on");
        }
      }
    };
    thread.start();
  }

  public void setElapsedSeconds(int elapsed_seconds) {
    this.elapsed_seconds = elapsed_seconds;
  }

  public Picture getPicture() {
    return image;
  }

}
