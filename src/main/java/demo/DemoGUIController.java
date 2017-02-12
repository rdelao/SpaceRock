package demo;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import sensor.SensorInterface;
import sensor.SensorSimulation;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.KeyFrame;

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


  @Override
  public void initialize(URL location, ResourceBundle resources) {

    sensor = new SensorSimulation();

    Timeline updateClock = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        updateCamStatusLabel();
        updateImageStatus();
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

  private void reset() {
    System.out.println("Reset Camera ON/OFF");
    sensor.reset();
  }

  private void takePicture() {
    //sensor.takePicture((int) zoomSlider.getMin());
    //imageView.setImage(SwingFXUtils.toFXImage(sensor.getImageChunk(i * 100 + 50, j * 100 + 50, 100), image));
    Task task = new Task<Void>() {
      @Override
      public Void call() throws Exception {
        sensor.takePicture((int) zoomSlider.getMin());
        Graphics2D g = buildable_image.createGraphics();
        for (int j = 0; j < 40; j++) {
          for (int i = 0; i < 40; i++) {
            g.drawImage(sensor.getImageChunk(i * 100 + 50, j * 100 + 50, 100), i * 100, j * 100, 100, 100, null);
            Platform.runLater(() -> imageView.setImage(SwingFXUtils.toFXImage(buildable_image, image)));
            Thread.sleep(100);
          }
        }
        return null;
      }
    };

    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }


}
