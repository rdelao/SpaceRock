package Commands;
/**
 * Created by magik on 2/5/2017.
 */

import java.io.Serializable;

public class Outgoing implements Serializable {
    double zoom;
    int sectorHeight;
    int sectorWidth;
    boolean image;

    public Outgoing(double zoom,int sectorHeight,int sectorWidth,boolean image){
        this.zoom=zoom;
        this.sectorHeight=sectorHeight;
        this.sectorWidth=sectorWidth;
        this.image=image;
    }

}
