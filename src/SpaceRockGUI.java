import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * @author Sahba and Kathrina
 */
public class SpaceRockGUI extends Application
{
  private PerspectiveCamera viewCamera;

  @Override
  public void start(Stage stage) throws Exception
  {
    //Parent root = FXMLLoader.load(getClass().getResource("SpaceRockGUI.fxml"));

    BorderPane mainPane = new BorderPane(createView());
    mainPane.setRight(createLeftPane());
    mainPane.setBottom(createButtom());
    Scene scene = new Scene(mainPane);
    stage.setScene(scene);
    stage.setTitle("Space Rock Control Center");
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

    gridPane.add(new Label("Terminal:"),5,0);
    TextArea terminalText=new TextArea("$>System Initialized\n$>");
    terminalText.setPrefColumnCount(20);
    terminalText.setPrefRowCount(10);
    terminalText.setEditable(false);
    gridPane.add(terminalText,5,1,20,10);
    return gridPane;
  }

  private SubScene createView()
  {
    viewCamera = new PerspectiveCamera(true);
    Group root = new Group();
    Sphere sphere = new Sphere(30);
    Sphere sphere2 = new Sphere(40);

    sphere.getTransforms().addAll(new Translate(300, 400, 10));
    PhongMaterial s1Material=new PhongMaterial();
    s1Material.setDiffuseMap(new Image("file:resources/2.png"));
    sphere.setMaterial(s1Material);
    sphere2.getTransforms().addAll(new Translate(100, 40, 20));
    PhongMaterial m = new PhongMaterial();
    m.setDiffuseMap(new Image("file:resources/1.jpg"));
    sphere2.setMaterial(m);
    viewCamera.setTranslateZ(-10);
    root.getChildren().addAll(viewCamera, sphere, sphere2);
    SubScene scene = new SubScene(root, 600, 600);
    scene.setFill(Color.BLACK);
    return scene;
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    launch(args);
  }

}
