package memory;
/**
 * Created by ststromberg and erparks on 2/26/17.
 * Represents the current state of shared memory interfaced
 * by both the fpga and the pc.
 */

public class SharedMemory
{
    class FPGAMemory implements FPGFacing
    {

    }
    class CPUMemory implements CPUFacing
    {

    }
    class MutualMemory implements FPGFacing, CPUFacing
    {

    }
}