package sensor;

/**
 * Created by sean on 2/11/17.
 */
public enum ZoomLevel {
  NONE(0, 4000, 4000),
  LEVEL_1(1, 3000, 3000),
  LEVEL_2(2, 2000, 2000),
  LEVEL_3(3, 1000, 1000);

  private final int[] IMAGE_SIZE = {4000,4000};
  int value;
  // x and y values of the top left corner
  int x;
  int y;
  // width and height of original image to capture from starting x,y
  int width;
  int height;

  ZoomLevel(int value, int width, int height) {
    this.value = value;
    this.width = width;
    this.height = height;
    this.x = (IMAGE_SIZE[0] / 2) - width / 2;
    this.y = (IMAGE_SIZE[1] / 2) - height / 2;
  }

  public static ZoomLevel fromValue(int value) throws IllegalArgumentException {
    try {
      for (ZoomLevel zoom : ZoomLevel.values()) {
        if (zoom.value == value) {
          return zoom;
        }
      }
      throw new IllegalArgumentException("Unknown zoom level: " + value);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Unknown zoom level: " + value);
    }
  }
}
