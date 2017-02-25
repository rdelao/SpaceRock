/*
 * 
 */


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 *
 * @author keira
 */
public class SpaceRockFXMLController implements Initializable {
    
    //Sliders
    @FXML
    private Slider zoom_slide = new Slider(0, 100, 0);

    @FXML
    private Slider section_size_slide = new Slider(50, 400, 50);
    
    @FXML
    private Slider overlap_amount_slide = new Slider(50, 400, 50);
    
    //Text Fields
    @FXML
    private TextField zoom_txt = new TextField(Double.toString(zoom_slide.getValue()));
    
    @FXML
    private TextField section_size_txt = new TextField(Double.toString(section_size_slide.getValue()));

    @FXML
    private TextField overlap_amount_txt = new TextField(Double.toString(overlap_amount_slide.getValue()));

    // @FXML
    // private void handleZoomSlideAction(ActionEvent event) {
    //     zoom_txt.setText(Double.toString(zoom_slide.getValue()));
    // }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        zoom_txt.setText(Double.toString(0.000));
        section_size_txt.setText(Double.toString(100.0));
        overlap_amount_txt.setText(Double.toString(0.000));

        zoom_slide.valueProperty().addListener(event -> {
            zoom_txt.setText(Double.toString(zoom_slide.getValue()));
        });
        section_size_slide.valueProperty().addListener(event -> {
            section_size_txt.setText(Double.toString((section_size_slide.getValue() + 25) * 4));
        });
        overlap_amount_slide.valueProperty().addListener(event -> {
            overlap_amount_txt.setText(Double.toString((overlap_amount_slide.getValue()) * 2));
        });
    }    
    
}
