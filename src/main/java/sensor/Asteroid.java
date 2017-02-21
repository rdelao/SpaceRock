package sensor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Sean on 2/4/17.
 */
public class Asteroid {
  private static final int[] size_range = {500, 4000};
  private static final int[] distance_range = {100, 1000};
  private static final int[] speed_range = {-5, 5};
  private static final String[] asteroid_images = {"asteroid_1.png", "asteroid_2.png", "asteroid_3.png",
          "asteroid_4.png", "asteroid_5.png"};

  private static int nextid = 0;

  private int[] initial_location;
  private int size;
  private int[] trajectory;
  private int id;
  private BufferedImage image;
  private int created_at;
  //private HashMap<Integer, Integer[]> hist_location;

  public int[] current_location;
  public int current_radius;

  public Asteroid(int[] initial_location, int size, int[] trajectory, int elapsed_time) {
    this.initial_location = initial_location;
    this.size = size;
    this.trajectory = trajectory;
    this.created_at = elapsed_time;
    this.id = nextid;
    nextid += 1;
    setRandomImage();
  }

  public Asteroid(int[] loc_constraint, int elapsed_time) {
    Random rand = new Random();
    this.initial_location = new int[]{loc_constraint[0], loc_constraint[1], rand.nextInt(distance_range[1]) + distance_range[0]};
    this.size = rand.nextInt(size_range[1]) + size_range[0];
    this.trajectory = new int[]{rand.nextInt(speed_range[1]) + speed_range[0], rand.nextInt(speed_range[1]) + speed_range[0], rand.nextInt(speed_range[1]) + speed_range[0]};
    this.current_location = initial_location;
    this.current_radius = size / 2;
    this.created_at = elapsed_time;
    this.id = nextid;
    nextid += 1;
    setRandomImage();
  }

  public int getId() {
    return this.id;
  }

  public void move(int elapsed_seconds) {
    this.current_location = location(elapsed_seconds);
    this.current_radius = radius(elapsed_seconds);
  }

  public int[] location(int elapsed_seconds) {
    return new int[]{initial_location[0] + trajectory[0] * (elapsed_seconds - created_at),
                     initial_location[1] + trajectory[1] * (elapsed_seconds - created_at),
                     initial_location[2] + trajectory[2] * (elapsed_seconds - created_at)};
  }

  public int radius(int elapsed_seconds) {
    if (location(elapsed_seconds)[2] == 0) {
      return 4000;
    }
    return (int)((size / 2.0) / (location(elapsed_seconds)[2] / 100.0));
  }

  public BufferedImage getImage() {
    return image;
  }

  @Override
  public String toString() {
    return "Asteroid{" +
        "id=" + id +
        ", initial_location=" + Arrays.toString(initial_location) +
        ", size=" + size +
        ", trajectory=" + Arrays.toString(trajectory) +
        ", created_at=" + created_at +
        ", current_location=" + Arrays.toString(current_location) +
        ", current_radius=" + current_radius +
        '}';
  }

  private void setRandomImage() {
    Random rand = new Random();
    String asteroid_image = asteroid_images[rand.nextInt(asteroid_images.length)];
    System.out.format("setting imageView to: %s\n", asteroid_image);
    try {

      this.image = ImageIO.read(Asteroid.class.getResource("asteroids/" + asteroid_image));
    } catch (IOException e) {
    }
  }
}
