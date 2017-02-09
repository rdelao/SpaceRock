import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Sean on 2/4/17.
 */
public class Camera implements Sensorable {
  private static final int[] image_size = {4000,4000};
  private static final int MAX_ASTEROIDS = 5;
  private static final int MAX_STORED_PICTURES = 5;

  private HashMap<Integer, Picture> images = new HashMap<>();
  private int elapsed_seconds = 0;
  private ArrayList<Asteroid> asteroids = new ArrayList<>();

  /**
   * Initialize a camera object
   */
  public Camera() {
    this.images = new HashMap<>();
  }

  /**
   * Returns the status of the Camera.
   * @return
   */
  public int status() {
    return 0;
  }

  /**
   * Notifies us to take a picture with a zoom level (1-3) ??
   * Returns a generated id
   * @param zoom
   * @return
   */
  public int take_picture(int zoom) {
    // Check to make sure we have enough storage space to save another picture. If not, then remove the oldest picture.
    if (images.size() >= MAX_STORED_PICTURES) {
      System.out.println("OUT OF MEMORY ERROR: removing oldest picture before taking another...");
      int oldest_id = Collections.min(images.keySet());
      images.remove(oldest_id);
      System.out.format("Removed picture %d\n", oldest_id);
    }

    // Take a new picture
    Picture picture = generate_image(elapsed_seconds, zoom);
    images.put(picture.getId(), picture);
    return picture.getId();
  }

  public int take_picture() {
    return take_picture(0);
  }

  public void set_elapsed_seconds(int elapsed_seconds) {
    this.elapsed_seconds = elapsed_seconds;
  }

  /**
   * Returns a chunk of an image with the given id
   * @param id
   * @param x
   * @param y
   * @param size
   * @return
   */
  public Image image_chunk(int id, int x, int y, int size) {
    Picture picture = images.get(id);
    return picture.chunk(x, y, size);
  }

  /**
   * Occasionally it may be necessary to get the whole picture object.
   * @param id
   * @return
   */
  public Picture getPicture(int id) {
    return images.get(id);
  }

  /**
   * Generate a image at the given time with the given zoom
   * @param time
   * @param zoom
   * @return
   */
  private Picture generate_image(int time, int zoom) {
    BufferedImage image = new BufferedImage(image_size[0], image_size[1], BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    g.setColor(Color.black);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
    System.out.format("Generating picture at time: %d seconds:\n", time);

    // Generate a starting set of asteroids at time = 0
    if (elapsed_seconds == 0) {
      asteroids.add(new Asteroid(new int[]{200, 200, 500}, 500, new int[]{1, 1, -1}));
      generate_random_asteroids();
    }

    // If we no longer have our max number of asteroids then generate some more
    if (asteroids.size() < MAX_ASTEROIDS) {
      generate_random_asteroids();
    }

    // Move all asteroids and draw them on the image
    for (Asteroid asteroid : asteroids) {
      asteroid.move(time);
      System.out.println(asteroid.toString());
      draw_asteroid_to_image(image, asteroid);
    }

    // Remove all asteroids that will never show up again
    remove_off_window_asteroids();

    return new Picture(image);
  }

  private void remove_off_window_asteroids() {
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

  private void generate_random_asteroids() {
    while(asteroids.size() <= MAX_ASTEROIDS) {
      asteroids.add(new Asteroid(image_size));
    }
  }

  private void draw_asteroid_to_image(BufferedImage image, Asteroid asteroid) {
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

  public static void main(String[] args) {
    Camera camera = new Camera();

    for (int i = 0; i < 10; i++) {
      camera.set_elapsed_seconds(i * 30);
      int picture_id = camera.take_picture();
      System.out.format("picture %d was taken\n", picture_id);
      Picture picture = camera.getPicture(picture_id);
      File output_file = new File("generated_image_" + picture.getId() + ".png");
      try {
        ImageIO.write(picture.get_image(), "png", output_file);
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.format("Output picture %d to generated_image_%d.png\n", picture_id, picture_id);
    }
  }
}
