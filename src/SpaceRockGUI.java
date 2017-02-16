import Commands.Asteroid;
import Commands.AsteroidData;
import Commands.IncomingListener;
import javafx.geometry.Insets;
import Network.Connection;
import Network.DummySat;
import Processing.DebrisProcessor;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sahba and Kathrina
 */
public class SpaceRockGUI extends Application implements IncomingListener
{
  private final DebrisProcessor processor = new DebrisProcessor();
  private final Connection netLink = new Connection();
  private final DummySat satellite = new DummySat();
  TextArea terminalText;
  private PerspectiveCamera viewCamera;
  private Group rockGroup = new Group();
  private Asteroid[] lastFrame = null;
  private boolean newData = false;
  private double x0 = 0;
  private double y0 = 0;
  private static final int CAMERA_ZOOM_COEF = 100;
  private AnimationTimer timer = new AnimationTimer()
  {
    @Override
    public void handle(long now)
    {
      if (newData)
      {
        ObservableList<Node> children = rockGroup.getChildren();
        children.clear();
        children.add(viewCamera);
        children.addAll(getAsteroidNodes());
        newData = false;
      }
    }
  };


  @Override
  public void start(Stage stage) throws Exception
  {
    SubScene view = createView();
    view.setOnScroll(new EventHandler<ScrollEvent>()
    {
      @Override
      public void handle(ScrollEvent event)
      {
        viewCamera.setTranslateZ(viewCamera.getTranslateZ() + event.getDeltaY());
      }
    });
    satellite.start();
    netLink.addIncomingListener(this);
    netLink.connectToDummySat();
    timer.start();
    BorderPane mainPane = new BorderPane(view);
    mainPane.setRight(createLeftPane());
    mainPane.setBottom(createButtom());
    Scene scene = new Scene(mainPane);
    stage.setScene(scene);
    stage.setTitle("Space Rock Control Center");
    //set textarea here
    view.setOnScroll((ScrollEvent event) ->
                       viewCamera.setTranslateZ(viewCamera.getTranslateZ() + event.getDeltaY()));
    view.setOnMousePressed((MouseEvent ev) ->
    {

      x0 = ev.getX();
      y0 = ev.getY();
    });
    view.setOnMouseReleased((
                              MouseEvent ev) ->
    {
      if (x0 != 0)
      {

        viewCamera.setTranslateX(viewCamera.getTranslateX() + x0 - ev.getX());
        x0 = 0;
      }
      if (y0 != 0)
        viewCamera.setTranslateY(viewCamera.getTranslateY() + y0 - ev.getY());
      y0 = 0;

    });
    stage.show();
  }


  private Node createLeftPane()
  {
    /*Terminal design section-- design status labels, console and button*/
    VBox connectionStatusVbox = new VBox(10);
    connectionStatusVbox.setPadding(new Insets(0, 5, 5, 15));
    Label statusLabel = new Label("  Connection Status  ");
    statusLabel.setStyle("-fx-font-size: 14pt; -fx-font-family: calibri; -fx-font-weight: bold");
    Button statusButton = new Button("Active");

    BorderPane statusBox = new BorderPane();
    statusBox.setCenter(statusButton);
    statusButton.setStyle("-fx-background-color: Green;-fx-font-size:large");
    BorderPane outputPane = new BorderPane();

    outputPane.setTop(new Label("Output Terminal:"));
    terminalText = new TextArea("$>System Initialized\n");
    terminalText.setPrefColumnCount(20);
    terminalText.setPrefRowCount(10);
    terminalText.setEditable(false);
    outputPane.setCenter(terminalText);
    Button clearButton = new Button("Clear Terminal");
    clearButton.setOnAction(event -> terminalText.setText("$>"));
    outputPane.setBottom(clearButton);

    connectionStatusVbox.getChildren().addAll(statusLabel, statusBox, outputPane);




    /* camera controls section*/
    VBox camLabelsVbox = new VBox(10);
    VBox box = new VBox(5);
    box.setPadding(new Insets(5, 5, 5, 5));

    Label camControlLabel = new Label("  Camera Controls  ");
    camControlLabel.setStyle("-fx-font-size: 14pt; -fx-font-family: calibri; -fx-font-weight: bold");
    Label imgDetailLabel = new Label("  Image Details ");
    imgDetailLabel.setStyle("-fx-font-size: 11pt");
    HBox camZoomBox = new HBox(5);
    HBox secOverlapBox = new HBox(5);
    HBox secSizeBox = new HBox(5);
    VBox imgDetailBox = new VBox(2);
    imgDetailBox.setPadding(new Insets(0,5,5,15));

    Label camZoomLabel = new Label("Zoom");
    camZoomLabel.setStyle("-fx-font-size:9pt");
    Slider camZoomSlider = new Slider(-2, 2, 0);
    camZoomSlider.setShowTickLabels(true);
    camZoomSlider.setShowTickMarks(true);
    camZoomSlider.setMajorTickUnit(1);
    camZoomSlider.setMinorTickCount(1);

    Label overlapLabel = new Label("Section Overlap");
    overlapLabel.setStyle("-fx-font-size:9pt");
    TextField overlapTextField = new TextField ();
    overlapTextField.setPrefWidth(30);

    Label pxLabel = new Label("px");
    Label pxLabel2 = new Label("px");
    Label secSizeLabel = new Label ("Section Size");
    secSizeLabel.setStyle("-fx-font-size:9pt");

    TextField secTextField = new TextField ();
    secTextField.setPrefWidth(30);


    camZoomBox.getChildren().addAll(camZoomLabel,camZoomSlider);
    secOverlapBox.getChildren().addAll(overlapLabel,overlapTextField, pxLabel);
    secSizeBox.getChildren().addAll(secSizeLabel,secTextField, pxLabel2);
    imgDetailBox.getChildren().addAll(camZoomBox,secOverlapBox,secSizeBox);

    ////////////////////////////////
    Label modeLabel = new Label("Image Capture Mode:");
    modeLabel.setStyle("-fx-font-size:11pt");
    /* style radio buttons*/
    ToggleGroup modeGroup = new ToggleGroup();
    RadioButton autoMode = new RadioButton("Automatic");
    autoMode.setStyle("-fx-font-size:9pt");
    RadioButton manualMode = new RadioButton("Manual");
    manualMode.setStyle("-fx-font-size:9pt");
    autoMode.setSelected(true);
    modeGroup.getToggles().addAll(autoMode, manualMode);

    HBox modeBox = new HBox();
    modeBox.setPadding(new Insets(5, 5, 5, 10));
    Button modeSubmitButton = new Button("submit");
    modeBox.getChildren().addAll(modeSubmitButton);



    VBox modeVbox = new VBox(5);
    modeVbox.setPadding(new Insets(0,5,5,15));
    modeVbox.getChildren().addAll(modeLabel, autoMode, manualMode, modeBox);
    ////////////////////////




    // add all components to right pane
    camLabelsVbox.getChildren().addAll(imgDetailLabel,imgDetailBox);
    box.getChildren().addAll(connectionStatusVbox,camControlLabel,camLabelsVbox,modeVbox);
    return box;
  }


  private Node createButtom()
  {
    GridPane gridPane = new GridPane();
    gridPane.setHgap(0);
    gridPane.setVgap(0);
    gridPane.setGridLinesVisible(false);
    //gridPane.setGridLinesVisible(true);
    gridPane.setPadding(new Insets(0, 10, 5, 150));

    /////////////////////////////frame zoom links here/////////////
    VBox framePanelVBox = new VBox(5);
    Label gridLabel = new Label("Frame Controls");
    gridLabel.setStyle("-fx-font-size: 14pt; -fx-font-family: calibri; -fx-font-weight: bold");
    HBox frameZoomBox = new HBox();
    frameZoomBox.setAlignment(Pos.CENTER);
    HBox frameZoomElements = new HBox();
    Label zoomLabel = new Label("Frame Zoom:");
    zoomLabel.setStyle("-fx-font-size: 9pt; ");
    Slider zoomSlider = new Slider(-5, 5, 0);
    zoomSlider.setShowTickLabels(true);
    zoomSlider.setShowTickMarks(true);
    zoomSlider.setMajorTickUnit(1);
    zoomSlider.setMinorTickCount(1);

    //create buttons and add to grid pane
    GridPane frameButtons = new GridPane();
    Button upButton = new Button("Up");
    Button downButton = new Button("Down");
    Button leftButton = new Button("Left");
    Button rightButton = new Button("Right");
    rightButton.setPrefWidth(55);
    upButton.setPrefWidth(55);
    downButton.setPrefWidth(55);
    leftButton.setPrefWidth(55);
    leftButton.setStyle("-fx-font-size:8pt");
    upButton.setStyle("-fx-font-size:8pt");
    downButton.setStyle("-fx-font-size:8pt");
    rightButton.setStyle("-fx-font-size:8pt");
    frameButtons.add(upButton, 2, 1);
    frameButtons.add(downButton, 2, 3);
    frameButtons.add(leftButton, 1, 2);
    frameButtons.add(rightButton, 3, 2);


     frameZoomElements.getChildren().addAll(zoomLabel,zoomSlider);
    framePanelVBox.getChildren().addAll(gridLabel, frameZoomElements,frameButtons);
    frameZoomBox.getChildren().addAll(framePanelVBox);
    gridPane.add(frameZoomBox, 1, 1);
    zoomSlider.valueProperty().addListener(

      (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
      {

        viewCamera.setTranslateZ(viewCamera.getTranslateZ() + (newValue.doubleValue() - oldValue.doubleValue()) * CAMERA_ZOOM_COEF);

      });
    return gridPane;
  }


  private SubScene createView()
  {
    viewCamera = new PerspectiveCamera(false);
    rockGroup.getChildren().add(viewCamera);
    SubScene scene = new SubScene(rockGroup, 600, 400);
    scene.setFill(Color.BLACK);
    scene.setCamera(viewCamera);
    viewCamera.setTranslateZ(-500);
    return scene;
  }


  /**
   * Convert all Asteroids to renderable Spheres and return them in a List
   *
   * @return List of Asteroid Spheres
   */
  private List<Node> getAsteroidNodes()
  {
    List<Node> nodeList = new ArrayList<>(lastFrame.length * 2);
    for (Asteroid a : lastFrame)
    {
      Sphere sphere = makeAsteroidSphere(a);
      nodeList.add(sphere);
    }
    return nodeList;
  }


  /**
   * Make a Sphere representing some Asteroid
   *
   * @param a Asteroid to use as a basis
   * @return a Sphere with the asteroid's ID drawn on it
   */
  private Sphere makeAsteroidSphere(Asteroid a)
  {
    Sphere s = new Sphere(a.size);
    PhongMaterial mat = new PhongMaterial(Color.BURLYWOOD);
    s.setTranslateX(a.getLoc().getX());
    s.setTranslateY(a.getLoc().getY());
    s.setMaterial(mat);
    s.setOnMouseEntered(event ->
    {
      terminalText.appendText(String.format("$> %s%n", a.toString()));
    });
    return s;
  }


  @Override
  public void newAsteroidData(AsteroidData[] asteroids, long timestamp)
  {
    lastFrame = asteroidsFromData(asteroids);
    processor.addAndAssign(lastFrame);
    newData = true;
  }


  /* Convert an AsteroidData array to an array of Asteroids prepared for the DebrisProcessor */
  private Asteroid[] asteroidsFromData(AsteroidData[] data)
  {
    Asteroid[] asteroids = new Asteroid[data.length];
    for (int i = 0; i < data.length; i++)
    {
      AsteroidData d = data[i];
      asteroids[i] = new Asteroid(d.getLoc(), 0, d.getSize());
    }
    return asteroids;
  }


  @Override
  public void newImageData(java.awt.Image img, long id)
  {
        /* TODO: Something meaningful here */
    System.out.println("Got new image!");
  }
}
