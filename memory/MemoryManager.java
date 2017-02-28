/**
 * Created by Ethan on 2/28/2017.
 */
public interface MemoryManager {

    //Methods to be called by the FPGA
    boolean getRawImageRequested();

    //Methods to be called by the PC
    void setRawImageRequested();
}
