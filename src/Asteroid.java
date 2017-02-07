import java.util.HashMap;
import java.util.Random;

/**
 * Created by Sean on 2/4/17.
 */
public class Asteroid {
  private static final int[] size_range = {500, 4000};
  private static final int[] distance_range = {100, 2000};
  private static final int[] speed_range = {-5, 5};

  private int[] initial_location;
  private int size;
  private int[] trajectory;
  private HashMap<Integer, Integer[]> hist_location;

  public int[] current_location;
  public int current_radius;

  public Asteroid(int[] initial_location, int size, int[] trajectory) {
    this.initial_location = initial_location;
    this.size = size;
    this.trajectory = trajectory;
  }

  public Asteroid(int[] loc_constraint) {
    Random rand = new Random();
    this.initial_location = new int[]{rand.nextInt(loc_constraint[0]), rand.nextInt(loc_constraint[1]), rand.nextInt(distance_range[1]) + distance_range[0]};
    this.size = rand.nextInt(size_range[1]) + size_range[0];
    this.trajectory = new int[]{rand.nextInt(speed_range[1]) + speed_range[0], rand.nextInt(speed_range[1]) + speed_range[0], rand.nextInt(speed_range[1]) + speed_range[0]};
    this.current_location = initial_location;
    this.current_radius = size / 2;
  }

  public void move(int elapsed_seconds) {
    this.current_location = location(elapsed_seconds);
    this.current_radius = radius(elapsed_seconds);
  }

  public int[] location(int elapsed_seconds) {
    return new int[]{initial_location[0] + trajectory[0] * elapsed_seconds,
                     initial_location[1] + trajectory[1] * elapsed_seconds,
                     initial_location[2] + trajectory[2] * elapsed_seconds};
  }

  public int radius(int elapsed_seconds) {
    if (location(elapsed_seconds)[2] == 0) {
      return 4000;
    }
    return (size / 2) / (location(elapsed_seconds)[2] / 10);
  }
}
