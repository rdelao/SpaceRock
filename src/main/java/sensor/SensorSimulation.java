package sensor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

  private boolean status;

  private boolean captureStatus;

  /**
   * Initialize a camera object
   */
  public SensorSimulation() {

  }

  /**
   * Returns the status of the SensorSimulation.
   * @return true if camera is operational
   */
  public synchronized boolean status() {
    return status;
  }

  @Override
  public synchronized boolean captureStatus() {
    return captureStatus;
  }

  /**
   * Notifies us to take a picture with a zoom level (0-3) ??
   * @param zoom the zoom setting 0-3
   */
  public void takePicture(int zoom) {
    // Take a new picture
    image = generateImage(elapsed_seconds, zoom);
  }

  public void takePicture() {
    takePicture(0);
  }

  @Override
  public Image getImageChunk(int x, int y, int size) {
    return null;
  }

  @Override
  public void on() {

  }

  @Override
  public void off() {

  }

  @Override
  public void reset() {

  }

  public void setElapsedSeconds(int elapsed_seconds) {
    this.elapsed_seconds = elapsed_seconds;
  }

  /**
   * Returns a chunk of an image with the given id
   * @param x
   * @param y
   * @param size
   * @return
   */
  public Image imageChunk(int x, int y, int size) {
    return image.chunk(x, y, size);
  }

  /**
   * Used for simulation testing only
   * @return entire image
   */
  public Picture getPicture() {
    return image;
  }

  /**
   * Generate a image at the given time with the given zoom
   * @param time seconds since last picture
   * @param zoom scale 0-3
   * @return generated Picture object
   */
  private Picture generateImage(int time, int zoom) {
    BufferedImage image = new BufferedImage(image_size[0], image_size[1], BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    g.setColor(Color.black);
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

    generateNoise(image);

    // Move all asteroids and draw them on the image
    for (Asteroid asteroid : asteroids) {
      asteroid.move(time);
      System.out.println(asteroid.toString());
      drawAsteroidToImage(image, asteroid);
    }

    // Remove all asteroids that will never show up again
    removeOffWindowAsteroids();

    return new Picture(image);
  }

  private void generateNoise(BufferedImage image) {
    return;
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
