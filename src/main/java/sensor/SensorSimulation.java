package sensor;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sean on 2/4/17.
 */
public class SensorSimulation implements SensorInterface {

  private static final int TIME_STEP = 30;

  private Picture image;
  private ImageGenerator image_generator;
  private int elapsed_seconds = 0 - TIME_STEP;

  private Boolean camera_ready = false;

  private Boolean image_ready = false;

  private boolean auto_advance = true;
  private ZoomLevel zoom_level = ZoomLevel.NONE;
  private Boolean taking_picture = false;
  private Boolean frame_ready = false;
  private BufferedImage frame = null;

  /**
   * Initialize a camera object
   */
  public SensorSimulation() {
    this(true);
  }

  /**
   * For testing only
   * @param autoAdvance true to increment with takePicture
   */
  public SensorSimulation(boolean autoAdvance){
    this.auto_advance = autoAdvance;
    resetDefaults();
  }

  @Override
  public boolean ready() {
    return camera_ready;
  }

  @Override
  public boolean imageReady() {
    return image_ready;
  }

  @Override
  public void takePicture() {
    synchronized (taking_picture) {
      if (!camera_ready || taking_picture) {
        System.out.println("Already taking a picture");
        return;
      }
      taking_picture = true;
    }
    Thread thread = new Thread() {
      @Override
      public void run() {
        taking_picture = true;
        synchronized (image_ready) {
          if(auto_advance){
            elapsed_seconds += TIME_STEP;
          }

          // Take a new picture
          System.out.println("Taking new picture at zoom level: " + zoom_level);
          image = image_generator.generateImage(elapsed_seconds, zoom_level);
          image_ready = true;
          taking_picture = false;
        }
      }
    };
    image_ready = false;
    thread.start();

  }

  @Override
  public void setZoom(ZoomLevel zoom) {
    this.zoom_level = zoom;
  }

  @Override
  public synchronized void setFrame(int x, int y, int size) {
    if(!image_ready) {
      throw new ImageNotReadyException("There is no image to get a frame from");
    }
    this.frame = image.frame(x, y, size);
    this.frame_ready = true;
  }

  @Override
  public synchronized BufferedImage getFrame() {
    if (!frame_ready) {
      throw new FrameNotReadyException("Frame is currently not ready");
    }
    this.frame_ready = false;
    return frame;
  }

  @Override
  public void on() {
    Thread thread = new Thread() {
      @Override
      public void run() {
        synchronized (camera_ready) {
          System.out.println("Turning Sensor on...");
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          resetDefaults();
          camera_ready = true;
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
        synchronized (camera_ready) {

          System.out.println("Turning sensor off...");

          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          camera_ready = false;
          image = null;
          image_ready = false;
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
        synchronized (camera_ready) {
          System.out.println("Resetting Sensor...");
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          camera_ready = false;
          image_ready = false;
          System.out.println("Sensor off");

          resetDefaults();

          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          camera_ready = true;
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

  private void resetDefaults() {
    this.image_generator = new ImageGenerator();
    this.zoom_level = ZoomLevel.NONE;
    this.image = null;
    if (auto_advance) {
      this.elapsed_seconds = 0 - TIME_STEP;
    } else {
      this.elapsed_seconds = 0;
    }
    this.camera_ready = false;
    this.image_ready = false;
  }

}
