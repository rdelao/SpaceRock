package UI;/*
 * 
 */


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.util.Date;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author keira
 */
public class SpaceRockFXMLController implements Initializable {
    private double size;
    @FXML
    private Label timeCaptured;

    @FXML
    private Label objectID;

    @FXML
    private Label threatLevel;

    @FXML
    private Label velocity;

    @FXML
    private Label diameter;

    public void setData(double size) {
        this.size = size;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeCaptured.setText("Time Captured: "+(new Date()).toString());
        diameter.setText(Double.toString(this.size));
//        zoom_txt.setText(Double.toString(0.000));
//        section_size_txt.setText(Double.toString(100.0));
//        overlap_amount_txt.setText(Double.toString(0.000));
//
//        zoom_slide.valueProperty().addListener(event -> {
//            zoom_txt.setText(Double.toString(zoom_slide.getValue()));
//        });
//        section_size_slide.valueProperty().addListener(event -> {
//            section_size_txt.setText(Double.toString((section_size_slide.getValue() + 25) * 4));
//        });
//        overlap_amount_slide.valueProperty().addListener(event -> {
//            overlap_amount_txt.setText(Double.toString((overlap_amount_slide.getValue()) * 2));
//        });
    }    
    
}
