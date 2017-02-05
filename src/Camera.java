import java.awt.*;
import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * Created by Sean on 2/4/17.
 */
public class Camera implements Sensorable {
  private static int nextid = 0;
  private HashMap<Integer, Picture> images = new HashMap<>();
  private int elapsed_seconds = 0;
  private List<Asteroid> asteroids;

  /**
   * Initialize a camera object
   */
  public Camera() {
    this.images = new HashMap<Integer, Picture>();
  }

  /**
   * Returns the status of the Camera.
   *
   * @return
   */
  public int status() {

  }

  /**
   * Notifies us to take a picture with a zoom level (1-3) ??
   * Returns a generated id
   *
   * @param zoom
   * @return
   */
  public int take_picture(int zoom) {
    int id = nextid + 1;
    nextid += 1;
    Picture picture = generate_image(elapsed_seconds, zoom);
    images.set(id, picture);
    return id;
  }

  public int take_picture() {
    return take_picture(0);
  }


  /**
   * Returns a chunk of an image with the given id
   *
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
   * Generate a image at the given time with the given zoom
   *
   * @param time
   * @param zoom
   * @return
   */
  private Picture generate_image(int time, int zoom) {

  }

  private void write_asteroid_to_image(Image image, Asteroid asteroid) {
    int xmin = Array
  }
}
