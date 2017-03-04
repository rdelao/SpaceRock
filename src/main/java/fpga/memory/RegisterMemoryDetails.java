package fpga.memory;

/**
 * Created by T10 on 3/3/2017.
 *  Represents a class containing details about the registers physical layout in memory.
 */
public interface RegisterMemoryDetails
{
    public int getStartPosition();
    public int setStartPosition();
    public int getLength();
    public int setLength();
}