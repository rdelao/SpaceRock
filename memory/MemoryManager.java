
import java.awt.Point;
import memory.Asteroid;

/**
 * Created by ststromberg erparks on 2/28/2017.
 * Interface to be implemented by SharedMemory.java. Defines
 * methods for reading and writing memory.
 */
public interface MemoryManager {

    //Methods to be called by the FPGA
    boolean getCameraOn();
    boolean getReset();
    boolean getTakePicture();
    SensorStatus getStatus();
    int getZoom();
    int getFrameSize();
    Point getFrameLocation();
    void addAsteroid(Asteroid a);

    //Methods to be called by the PC
    void setRawImageRequested();
    void setSensorStatus(SensorStatus status);
    void setTakePicture();
    void setFrameSize();

    Asteroid getAsteroid();

}
