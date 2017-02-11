import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by jholland on 2/10/17.
 */
public class TestMain {

    public static void main(String[] args) {
        SensorSimulation camera = new SensorSimulation();
        for (int i = 0; i < 10; i++) {
            camera.setElapsedSeconds(i * 30);
            camera.takePicture();
            System.out.format("picture %d was taken\n", i);
            Picture picture = camera.getPicture();
            File output_file = new File("generated_image_" + i + ".png");
            try {
                ImageIO.write(picture.get_image(), "png", output_file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.format("Output picture %d to generated_image_%d.png\n", i, i);
        }
    }
}
