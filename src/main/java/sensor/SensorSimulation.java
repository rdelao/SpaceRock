package sensor;

import javafx.concurrent.Task;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sean on 2/4/17.
 */
public class SensorSimulation implements SensorInterface {
  private static final int[] image_size = {4000,4000};
  private static final int MAX_ASTEROIDS = 5;
  private static final int MAX_STORED_PICTURES = 5;
  private static final int TIME_STEP = 30;

  private Picture image;
  private int elapsed_seconds = 0;
  private ArrayList<Asteroid> asteroids = new ArrayList<>();

  private Boolean status = false;

  private Boolean captureStatus = false;

  private Random random = new Random();

  /**
   * Initialize a camera object
   */
  public SensorSimulation() {

  }

  /**
   * Returns the status of the SensorSimulation.
   * @return true if camera is operational
   */
  public boolean status() {
    synchronized (status) {
      return status;
    }
  }

  @Override
  public boolean captureStatus() {
    synchronized (captureStatus) {
      return captureStatus;
    }
  }

@Override
  public void takePicture(int zoom) {
    if (!status) {
      return;
    }
    Thread thread = new Thread() {
        @Override
        public void run() {
            captureStatus = false;
            ZoomLevel zoom_level = ZoomLevel.fromValue(zoom);

            // Take a new picture
            System.out.println("Taking new picture at zoom level: " + zoom_level);
            image = generateImage(elapsed_seconds, zoom_level);
            captureStatus = true;
        }
    };
    thread.start();

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
        System.out.println("Turning Sensor on...");
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        synchronized (status) {
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
        System.out.println("Turning sensor off...");
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        synchronized (status) {
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
    off();
    on();
  }

  public void setElapsedSeconds(int elapsed_seconds) {
    this.elapsed_seconds = elapsed_seconds;
  }

  /**
   * Used for simulation testing only
   * @return entire imageView
   */
  public Picture getPicture() {
    return image;
  }

  /**
   * Generate a imageView at the given time with the given zoom
   * @param time seconds since last picture
   * @param zoom scale 0-3
   * @return generated Picture object
   */
  private Picture generateImage(int time, ZoomLevel zoom) {
    BufferedImage image = new BufferedImage(image_size[0], image_size[1], BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    g.setColor(java.awt.Color.black);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
    System.out.format("Generating picture at time: %d seconds:\n", time);

    // Generate a starting set of asteroids at time = 0
    if (elapsed_seconds == 0) {
      asteroids.add(new Asteroid(new int[]{200, 200, 1000}, 2500, new int[]{5, 5, -5}, 0));
      generateRandomAsteroids();
    }

    // If we no longer have our max number of asteroids then generate some more
    if (asteroids.size() < MAX_ASTEROIDS) {
      generateRandomAsteroids();
    }

    // Move all asteroids and draw them on the imageView
    for (Asteroid asteroid : asteroids) {
      asteroid.move(time);
      System.out.println(asteroid.toString());
      drawAsteroidToImage(image, asteroid);
    }

    // Remove all asteroids that will never show up again
    removeOffWindowAsteroids();

    generateNoise(image);

    if (zoom != ZoomLevel.NONE) {
      image = image.getSubimage(zoom.x, zoom.y, zoom.width, zoom.height);
      Image tmp = image.getScaledInstance(image_size[0], image_size[1], Image.SCALE_SMOOTH);
      image = new BufferedImage(image_size[0], image_size[1], Image.SCALE_SMOOTH);
      Graphics2D g2d = image.createGraphics();
      g2d.drawImage(tmp, 0, 0, null);
      g2d.dispose();
    }

    return new Picture(image);
  }

  private void generateNoise(BufferedImage image) {
    for(int i = 0; i < image_size[0]; i++){
      for(int j = 0; j < image_size[1]; j++){
        if(random.nextInt(100) <= 10){
          int colorValue = random.nextInt(255);
          int color = new Color(colorValue, colorValue, colorValue).getRGB();
          image.setRGB(i,j,color);
        }
      }
    }
  }

  private void removeOffWindowAsteroids() {
    ArrayList<Asteroid> asteroids_to_be_removed = new ArrayList<>();
    for (Asteroid asteroid : asteroids) {
      // If the asteroid is behind us then no additional calculation is required
      if (asteroid.current_location[2] <= 0) {
        asteroids_to_be_removed.add(asteroid);
        //asteroids.remove(asteroid);
        continue;
      }

      int xmin = asteroid.current_location[0] - asteroid.current_radius;
      int xmax = asteroid.current_location[0] + asteroid.current_radius;
      int ymin = asteroid.current_location[1] - asteroid.current_radius;
      int ymax = asteroid.current_location[1] + asteroid.current_radius;

      // If the asteroid is currently out of bounds of the image_size then we know it is no longer going to show up.
      if (xmax < 0 || ymax < 0 || xmin > image_size[0] || ymin > image_size[1]) {
        //asteroids.remove(asteroid);
        asteroids_to_be_removed.add(asteroid);
      }
    }

    for (Asteroid asteroid : asteroids_to_be_removed) {
      System.out.format("Removing asteroid %d\n", asteroid.getId());
      asteroids.remove(asteroid);
    }
  }

  private void generateRandomAsteroids() {
    while(asteroids.size() <= MAX_ASTEROIDS) {
      asteroids.add(new Asteroid(image_size, elapsed_seconds));
    }
  }

  private void drawAsteroidToImage(BufferedImage image, Asteroid asteroid) {
    if (asteroid.current_location[2] <= 0) {
      System.out.format("Asteroid %d is now behind us.\n", asteroid.getId());
      return;
    }

    Graphics2D g = image.createGraphics();
    // Uncomment these 2 lines to draw circles:
    //g.setColor(Color.gray);
    //g.fillOval(asteroid.current_location[0] - asteroid.current_radius, asteroid.current_location[1] - asteroid.current_radius, asteroid.current_radius, asteroid.current_radius);

    // Uncomment this line to use the images:
    g.drawImage(asteroid.getImage(), asteroid.current_location[0] - asteroid.current_radius, asteroid.current_location[1] - asteroid.current_radius, asteroid.current_radius, asteroid.current_radius, null);

    g.setColor(Color.green);
    g.drawString(Integer.toString(asteroid.getId()), asteroid.current_location[0], asteroid.current_location[1]);
  }


}
