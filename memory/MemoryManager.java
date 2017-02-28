/**
 * Created by ststromberg erparks on 2/28/2017.
 * Interface to be implemented by SharedMemory.java. Defines
 * methods for reading and writing memory.
 */
public interface MemoryManager {

    //Methods to be called by the FPGA
    boolean getRawImageRequested();

    //Methods to be called by the PC
    void setRawImageRequested();

    void setSensorStatus(SensorStatus status);

    SensorStatus getSensorStatus();
}
