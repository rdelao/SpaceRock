
import java.awt.Point;
import memory.Asteroid;

/**
 * Created by ststromberg erparks on 2/28/2017.
 * Interface to be implemented by SharedMemory.java. Defines
 * methods for reading and writing memory.
 */
public interface MemoryManager {

    //Methods to be called by the FPGA
    boolean getCameraPowered();
    boolean getReset();
    boolean getTakePicture();
    SensorStatus getStatus();
    ZoomLevel getZoom();
    int getFrameSize();
    Point getFrameLocation();
    void addAsteroid(Asteroid a);

    //Called by both
    void setTakePicture(boolean take);

    //Methods to be called by the PC
    void setSensorStatus(SensorStatus status);
    void setFrameSize(int size);
    void setFrameLocation(Point point);
    void setCameraPowered(boolean powerState);
    void setZoom(ZoomLevel level);
    void setReset(boolean reset);
    Asteroid getAsteroid();

}
