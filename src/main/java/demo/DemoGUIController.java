package demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;

/**
 * Created by jholland on 2/11/17.
 */
public class DemoGUIController {
    @FXML
    public Slider zoomSlider;
    @FXML
    public ImageView image;

    @FXML
    public ToggleButton turnOnCam;

    @FXML
    public Button takePicture;
    @FXML
    public Button reset;
}
