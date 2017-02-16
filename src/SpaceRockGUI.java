import Commands.Asteroid;
import Commands.AsteroidData;
import Commands.IncomingListener;
import Network.Connection;
import Network.DummySat;
import Processing.DebrisProcessor;
import javafx.animation.AnimationTimer;
import Util.Rock;
import javafx.application.Application;
import javafx.collections.ObservableList;
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
  private PerspectiveCamera viewCamera;
  private Group rockGroup = new Group();
  TextArea terminalText;

    private final DebrisProcessor processor = new DebrisProcessor();
    private final Connection netLink = new Connection();
    private final DummySat satellite = new DummySat();
    private Asteroid[] lastFrame = null;
    private boolean newData = false;
    private AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (newData) {
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
    Rock.setTextArea(terminalText);
    stage.show();
  }



    private Node createLeftPane() {
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


    private Node createButtom() {
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


    private SubScene createView() {
        viewCamera = new PerspectiveCamera(true);
        rockGroup.getChildren().add(viewCamera);
        SubScene scene = new SubScene(rockGroup, 600, 600);
        scene.setFill(Color.BLACK);
        return scene;
    }


    /**
     Convert all Asteroids to renderable Spheres and return them in a List

     @return List of Asteroid Spheres
     */
    private List<Node> getAsteroidNodes() {
        List<Node> nodeList = new ArrayList<>(lastFrame.length * 2);
        for (Asteroid a : lastFrame) {
            Sphere sphere = makeAsteroidSphere(a);
            nodeList.add(sphere);
        }
        return nodeList;
    }


    /**
     Make a Sphere representing some Asteroid

     @param a Asteroid to use as a basis

     @return a Sphere with the asteroid's ID drawn on it
     */
    private Sphere makeAsteroidSphere(Asteroid a) {
        Sphere s = new Sphere(a.size);
        PhongMaterial mat = new PhongMaterial(Color.BURLYWOOD);
        s.setTranslateX(a.getLoc().getX());
        s.setTranslateY(a.getLoc().getY());
        s.setMaterial(mat);
        s.setOnMouseClicked(event -> {
            terminalText.appendText(String.format("> %s%n", a.toString()));
        });
        return s;
    }


    @Override
    public void newAsteroidData(AsteroidData[] asteroids, long timestamp) {
        lastFrame = asteroidsFromData(asteroids);
        processor.addAndAssign(lastFrame);
        newData = true;
    }


    /* Convert an AsteroidData array to an array of Asteroids prepared for the DebrisProcessor */
    private Asteroid[] asteroidsFromData(AsteroidData[] data) {
        Asteroid[] asteroids = new Asteroid[data.length];
        for (int i = 0; i < data.length; i++) {
            AsteroidData d = data[i];
            asteroids[i] = new Asteroid(d.getLoc(), 0, d.getSize());
        }
        return asteroids;
    }


    @Override
    public void newImageData(java.awt.Image img, long id) {
        /* TODO: Something meaningful here */
        System.out.println("Got new image!");
    }
}
