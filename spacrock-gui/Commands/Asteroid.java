package Commands;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Created by magik on 2/5/2017.
 */
public class  Asteroid implements Serializable{
    Point2D location;
    Point2D heading;
    int id;
    double size;
    Image rawImage;


    public Asteroid(Point2D location,Point2D heading,int id,double size, Image rawImage){
        this.location=location;
        this.heading=heading;
        this.id=id;
        this.size=size;
        this.rawImage = rawImage;
    }
}
