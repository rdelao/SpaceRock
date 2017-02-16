package Util;

import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

/**
 * Created by sahba on 2/15/17.
 */
public class Rock extends Sphere {
    private final int id;
    private double velocity;
    private String heading;
    private double radius;
    private int x;
    private int y;
    private static TextArea area = null;

    public static void setTextArea(TextArea area) {
        if (Rock.area == null) {
            Rock.area = area;
        }
    }

    public void setImage(String url) {
        ((PhongMaterial) getMaterial()).setDiffuseMap(new Image(url));
    }

    public Rock(int id, double velocity, int x, int y, int z, double radius) {

        super(radius);
        getTransforms().addAll(new Translate(x, y, z));
        this.id = id;
        this.velocity = velocity;
        this.radius = radius;
        this.x=x;
        this.y=y;
        setMaterial(new PhongMaterial());
        setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (area != null) {
                    area.setText(area.getText()  + getString());
                }
            }
        });
    }

    public void setHeading() {
        this.heading = heading;
    }

    public Rock(int id, double velocity, int x, int y, int z, double radius, String heading) {

        this(id, velocity, x, y, z, radius);
        this.heading = heading;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rock rock = (Rock) o;

        return id == rock.id;
    }

    public void refreshTooltip() {

    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getString() {
        return "Rock{" +
                "id=" + id + " ," +
                " ( " + x + ", " + y + ")\n" +
                ",radius: " + radius +
                ", velocity=" + velocity +
                ", heading='" + heading +
                '}'+"\n$>";
    }
}
