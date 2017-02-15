package demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.util.Duration;
import sensor.SensorInterface;
import sensor.SensorSimulation;

import java.awt.*;
import java.awt.image.BufferedImage;
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

  private BufferedImage buildable_image = new BufferedImage(4000, 4000, BufferedImage.TYPE_INT_ARGB);

  @FXML
  public ToggleButton turnOnCam;

  @FXML
  public Label statusLabel; //Whether cam is on or off

  @FXML
  public Label imageStatus; //Whether image is available or not

  @FXML
  public Button takePicture;
  @FXML
  public Button reset;

  private SensorInterface sensor;

  private boolean previousCaptureStatus;
  private boolean loadimage;
    /**
     * Used for chunkifying the image.
     */
  private int i, j;

  private int elapsed_time = -30;


    @Override
  public void initialize(URL location, ResourceBundle resources) {

    sensor = new SensorSimulation();

    Timeline updateClock = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        updateCamStatusLabel();
        updateImageStatus();
        updateImage();
      }
    }));
    updateClock.setCycleCount(Timeline.INDEFINITE);
    updateClock.play();




    //Keep the slider as integer
    zoomSlider.valueProperty().addListener((observable, oldValue, newValue) ->
        zoomSlider.setValue(newValue.intValue()));

    takePicture.setOnAction(event -> takePicture());
    reset.setOnAction(event -> reset());
    turnOnCam.setOnAction(event -> toggleOnOff());

  }

  private void toggleOnOff() {
      imageView.setImage(null);
      buildable_image = new BufferedImage(4000, 4000, BufferedImage.TYPE_INT_ARGB);
    System.out.println("Toggled");
    if (this.turnOnCam.isSelected()) {
        sensor.on();
    } else {
        sensor.off();
    }
  }

  //Temporary update status for camera state
  private void updateCamStatusLabel(){
      if(sensor.status()) statusLabel.setText("Status: On");
      else statusLabel.setText("Status: Off");
  }

  private void updateImageStatus(){
    if(sensor.captureStatus()) imageStatus.setText("Image: READY");
    else imageStatus.setText("Image: NOT READY");
  }

  private void updateImage(){
      boolean status = sensor.captureStatus();
      if(status != previousCaptureStatus) {
          loadimage = true;
          i = 0;
          j = 0;
      }
      previousCaptureStatus = status;

      if(loadimage){
          BufferedImage chunk = sensor.getImageChunk(i * 200 + 100, j *200 + 100, 200);

          if (chunk != null) {
              Graphics2D g = buildable_image.createGraphics();
              g.drawImage(chunk, i * 200, j * 200, 200, 200, null);
              imageView.setImage(SwingFXUtils.toFXImage(buildable_image, image));

              i++;
              if (i >= 20) {
                  i = 0;
                  j++;
                  if (j >= 20) loadimage = false;
              }
          }
      }
  }

  private void reset() {
    System.out.println("Reset Camera ON/OFF");
      imageView.setImage(null);
      buildable_image = new BufferedImage(4000, 4000, BufferedImage.TYPE_INT_ARGB);
    sensor.reset();
  }

  private void takePicture() {
    sensor.takePicture((int) zoomSlider.getValue());
  }


}
