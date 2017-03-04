package fpga.memory;

/**
 * Created by T10 on 3/3/2017.
 *
 * To be thrown by MemoryMap when trying to write register to memory if register
 * flag indicates it is unavailable.
 */
public class FlagNotSetException extends Exception {
    private RegisterBase register;
    public FlagNotSetException(RegisterBase register)
    {
        super("Register flag for: "+register.getName()+" not set implying data not yet available.");
    }
}