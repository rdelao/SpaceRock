import Util.Rock;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Sahba and Kathrina
 */
public class SpaceRockGUI extends Application
{
  private PerspectiveCamera viewCamera;
  private Group rockGroup;
  TextArea terminalText;

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
    BorderPane mainPane = new BorderPane(view);
    mainPane.setRight(createLeftPane());
    mainPane.setBottom(createButtom());
    Scene scene = new Scene(mainPane);
    stage.setScene(scene);
    stage.setTitle("Space Rock Control Center");
    //set textarea here
    Rock.setTextArea(terminalText);
    stage.show();
  }

  private Node createLeftPane()
  {
    VBox box = new VBox(5);
    box.setPadding(new Insets(5, 5, 5, 5));
    Label statusLabel = new Label("  Connection Status:  ");
    statusLabel.setStyle("-fx-font-size:large");
    Button statusButton = new Button("Active");

    HBox statusBox = new HBox();
    statusBox.setPadding(new Insets(5, 5, 5, 40));
    statusBox.getChildren().addAll(statusButton);

    statusButton.setStyle("-fx-background-color: Green;-fx-font-size:large");

    Label modeLabel = new Label("Operation Mode:");
    modeLabel.setStyle("-fx-font-size:large");
    ToggleGroup modeGroup = new ToggleGroup();
    RadioButton autoMode = new RadioButton("Automatic");
    RadioButton manualMode = new RadioButton("Manual");
    autoMode.setSelected(true);
    modeGroup.getToggles().addAll(autoMode, manualMode);

    HBox modeBox = new HBox();
    modeBox.setPadding(new Insets(5, 5, 5, 40));
    Button modeSubmitButton = new Button("submit");
    modeBox.getChildren().addAll(modeSubmitButton);
    box.getChildren().addAll(statusLabel, statusBox, modeLabel, autoMode, manualMode, modeBox);
    return box;
  }

  private Node createButtom()
  {
    GridPane gridPane = new GridPane();
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setGridLinesVisible(false);
    //gridPane.setGridLinesVisible(true);
    gridPane.setPadding(new Insets(5, 10, 5, 10));
    Label gridLabel = new Label("Camera Parameters:");
    gridLabel.setStyle("-fx-font-size:large");
    Label zoomLabel = new Label("Zoom:");
    Slider zoomSlider = new Slider(-2, 2, 0);
    zoomSlider.setShowTickLabels(true);
    zoomSlider.setShowTickMarks(true);
    zoomSlider.setMajorTickUnit(1);
    zoomSlider.setMinorTickCount(1);
    gridPane.add(gridLabel, 0, 0, 3, 1);
    gridPane.add(zoomLabel, 0, 1);
    gridPane.add(zoomSlider, 1, 1, 3, 1);
    Button zoomSubmit = new Button("Submit");
    gridPane.add(zoomSubmit, 4, 1);

    gridPane.add(new Label("Image Size:"), 0, 2);
    TextField imageSizeField = new TextField();
    gridPane.add(imageSizeField, 2, 2);
    Button requestImageButton = new Button("Request");
    gridPane.add(requestImageButton, 4, 2);

    gridPane.add(new Label("Terminal:"), 5, 0);

    terminalText = new TextArea("$>System Initialized\n$>");
    terminalText.setPrefColumnCount(20);
    terminalText.setPrefRowCount(10);
    terminalText.setEditable(false);
    gridPane.add(terminalText, 5, 1, 20, 10);
    Button clearButton = new Button("Clear Terminal");
    clearButton.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        terminalText.setText("$>");
      }
    });
    gridPane.add(clearButton,8,11);
    return gridPane;
  }

  private SubScene createView()
  {
    viewCamera = new PerspectiveCamera(false);
    rockGroup = new Group();
    Group root = new Group();
    Rock sphere = new Rock(1, 5, 200, 50, 10, 50);
    Rock sphere2 = new Rock(2, 15, 10, 0, 10, 25);
    sphere.setImage("file:resources/1.jpg");
    sphere2.setImage("file:resources/2.png");

    viewCamera.setTranslateZ(-100);
    addObject(sphere);
    addObject(sphere2);
    root.getChildren().addAll(viewCamera, rockGroup);
    SubScene scene = new SubScene(root, 600, 600);
    scene.setFill(Color.BLACK);
    //viewCamera.setFarClip(1000);
    scene.setCamera(viewCamera);
    return scene;
  }

  void addObject(Rock rock)
  {
    rockGroup.getChildren().addAll(rock);
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    launch(args);
  }
}