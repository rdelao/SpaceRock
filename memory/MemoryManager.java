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
    boolean getStatus();
    int getZoom();

    void addAsteroid(Asteroid a);


    //Methods to be called by the PC
    void setRawImageRequested();

    void setTakePicture();
    void setZoom(int z);
    void setFrameSize();

    Asteroid getNextAsteroid();

}
