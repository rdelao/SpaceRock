package demo; /**
 * Created by jholland on 2/10/17.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Demo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("DemoGUI.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("SpaceRock SensorInterface Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.sizeToScene();
    }
}
