package sensor.demo;

import sensor.Picture;
import sensor.SensorSimulation;
import sensor.ZoomLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jholland on 2/10/17.
 */
public class TestMain {

  public static void main(String[] args) {
    SensorSimulation camera = new SensorSimulation(false);
    camera.on();
    while(!camera.ready()) {
      System.out.println("Waiting for camera to turn on..." + camera.ready());
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    for (int i = 0; i < 10; i++) {
      camera.setElapsedSeconds(i * 30);
      for (int zoom = 0; zoom < 4; zoom++) {
        takePictureAndWriteToFile(camera, zoom, i);
      }
    }
  }

  public static void takePictureAndWriteToFile(SensorSimulation camera, int zoom_level, int id) {
    camera.setZoom(ZoomLevel.fromValue(zoom_level));
    camera.takePicture();
    while(!camera.imageReady()) {
      System.out.println("Picture not ready yet...");
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.format("picture %d was taken at zoom_level %d\n", id, zoom_level);
    Picture picture = camera.getPicture();
    File output_file = new File("generated_image_" + id + "_zoom_level_" + zoom_level + ".png");
    writeImageToFile(picture.getImage(), output_file);
    System.out.format("Output picture %d with zoom level %d to %s\n", id, zoom_level, output_file.getName());
  }

  public static void writeImageToFile(BufferedImage image, File output_file) {
    try {
      ImageIO.write(image, "png", output_file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
