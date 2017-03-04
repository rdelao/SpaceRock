package fpga.memory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by T10 on 3/3/2017.
 *
 *This class is responsible for interacting with control registers
 * in order to insert and update their values into simulated memory.
 * This memory consists of a  byte array of serialized objects which represent registers.
 * TODO:
 * Make methods called from FPGA thread safe. (Brush up on locking)
 * Map registers to array of integers (as instructed in Roman's email)
 *
 * Comments:
 * Static methods because only one instance of memory is allowed for FPGA.
 * This means both the memory map and memory components are implemented statically.
 * Discuss if prefered if we implement as not static, and have main just instantiate one instace.
 *
 * Right now the flags are only partiallky implemented, and most likely incorrectly.
 * Need to clarify with Roman the behaviour of the flags, the flag is currently set in the interface RegisterBase.
 */
public class MemoryMap {

    // name of register maps to the location,and size in memory.
    private static Map<String,RegisterMemoryDetails> nameToRegisterMemoryDetails;

    /*
    * Called to allocate memory size.
     */
    private static void initMemoryMap(int size)
    {
        nameToRegisterMemoryDetails = new HashMap<>();
        Memory.initMemory(4000); //4k bytes for tesdting TODO: (will be replaced by size later)
    }

    /*
    * Returns if register is ready to be modified
     */
    public static boolean Ready(String registerName)
    {
        RegisterMemoryDetails regDetails=nameToRegisterMemoryDetails.get(registerName);
        RegisterBase registerBase = Memory.retrieveFromMemory(regDetails);
        return registerBase.isReady();
    }

    /*
    * Returns the register and resets the flag?
     */
    public static RegisterBase Read_And_Ack(String registerName) throws FlagNotSetException
    {
        RegisterMemoryDetails regDetails=nameToRegisterMemoryDetails.get(registerName);
        RegisterBase registerBase = Memory.retrieveFromMemory(regDetails);

        if(!registerBase.isReady())
        {
            throw new FlagNotSetException(registerBase);
        } else{
            registerBase.setReady(false);
            return registerBase;
        }
    }
}
