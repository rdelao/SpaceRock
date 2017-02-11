package demo;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import sensor.SensorInterface;
import sensor.SensorSimulation;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jholland on 2/11/17.
 */
public class DemoGUIController implements Initializable {
    @FXML
    public Slider zoomSlider;
    @FXML
    public ImageView imageView;

    private WritableImage image;

    @FXML
    public ToggleButton turnOnCam;

    @FXML
    public Button takePicture;
    @FXML
    public Button reset;

    private SensorInterface sensor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sensor = new SensorSimulation();

        //Keep the slider as integer
        zoomSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                zoomSlider.setValue(newValue.intValue()));

        takePicture.setOnAction(event -> takePicture());
        reset.setOnAction(event -> reset());
        turnOnCam.setOnAction(event -> toggleOnOff());

    }

    private void toggleOnOff(){
        System.out.println("Toggled");
    }

    private void reset(){
        System.out.println("Reset Camera ON/OFF");
    }

    private void takePicture(){
        sensor.takePicture((int)zoomSlider.getMin());
        imageView.setImage(SwingFXUtils.toFXImage(sensor.getImageChunk(2000, 2000, 4000), image));
    }
}
