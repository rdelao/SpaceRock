package memory;

import java.awt.Point;
import memory.Asteroid;

/**
 * Created by ststromberg erparks on 2/28/2017.
 * Interface to be implemented by SharedMemory.java. Defines
 * methods for reading and writing memory.
 */
public interface MemoryManager {
    boolean getCameraPowered();
    void setCameraPowered(boolean powerState);

    boolean getCameraReset();
    void setCameraReset(boolean reset);

    boolean getTakePicture(); //used by fpga when Camera Status is discrete
    void setTakePicture(boolean take); //set by cpu when Camera Status is discrete

    CameraStatus getCameraStatus();
    void setCameraStatus(CameraStatus status);

    ZoomLevel getCameraZoom();
    void setCameraZoom(ZoomLevel level);

    int getRequestedFrameSize();
    void setRequestedFrameSize(int size);

    Point getRequestedFrameLocation();
    void setRequestedFrameLocation(Point point);


    Asteroid getAsteroids();
    void addAsteroids(Asteroid a);

}
