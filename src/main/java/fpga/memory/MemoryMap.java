package fpga.memory;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
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
    private static HashMap<String, RegisterMemoryDetails> nameToRegisterMemoryDetails;

    private static HashMap<String, Boolean> readyFlags;

    private static HashMap<String, Boolean> emptyFlags;

  /**
   * Initializes the memory map
   * @throws MemoryInitializationException Thrown if errors are encountered
   */
    private static void initMemoryMap() throws MemoryInitializationException
    {
        nameToRegisterMemoryDetails = MemoryMapFactory.loadRegisterMap();
        HashMap<String, Boolean> readyMap = new HashMap<>();
        HashMap<String, Boolean> emptyMap = new HashMap<>();
        for (Map.Entry<String, RegisterMemoryDetails> entry : nameToRegisterMemoryDetails.entrySet())
        {
            readyMap.put(entry.getKey(), Boolean.TRUE);
            emptyMap.put(entry.getKey(), Boolean.TRUE);
        }
        readyFlags = readyMap;
        emptyFlags = emptyMap;
    }

  /** Read the value of a register
   *
   * @param clazz Type of the class you want to cast the object to. Very sensitive
   * @param registerName Name of the register to read
   * @param <T> Type of the class to cast
   * @return Object casted to type requested residing in the register with the provided name
   * @throws NoSuchRegisterFoundException Thrown if name doesnt exist in register namespace
   * @throws EmptyRegisterException thrown if register is empty when reading
   * @throws UnavailbleRegisterException thrown if register is currently busy
   * @throws ClassCastException Thrown if we had an error casting the return object
   */
    public static <T> T read(Class<T> clazz, String registerName)
        throws NoSuchRegisterFoundException,
        EmptyRegisterException,
        UnavailbleRegisterException,
        ClassCastException
    {
        if (isEmpty(registerName)) {
            throw new EmptyRegisterException("Attempting to read empty register.");
        } else if (!isReady(registerName)) {
            throw new UnavailbleRegisterException("Register is not ready to be accessed.");
        }
        setIsReady(registerName, false);
        Object object = null;
        for (Map.Entry<String, RegisterMemoryDetails> entry : nameToRegisterMemoryDetails.entrySet())
        {
            if (entry.getKey().equals(registerName)) {
              object = entry.getValue().getObject();
              entry.getValue().clearObject();
            }
        }
        if (object == null) {
            throw new NoSuchRegisterFoundException("No such register " + registerName);
        }
        setIsEmpty(registerName, true);
        setIsReady(registerName, true);
        return clazz.cast(object);
    }

  /**
   * Write to a control register
   * @param registerName Name of register to write to
   * @param value Object to write
   * @throws NoSuchRegisterFoundException Thrown if register does not exist
   * @throws UnavailbleRegisterException Thrown if register can not be written to
   * @throws ClassCastException Thrown if object given is not the type of the register
   */
    public static void write(String registerName, Object value)
      throws NoSuchRegisterFoundException,
      UnavailbleRegisterException,
      ClassCastException
    {
      if (!isEmpty(registerName) || !isReady(registerName)) {
        throw new UnavailbleRegisterException("Register can not be written to. Either not ready or currently full.");
      }
      setIsReady(registerName, false);
      for (Map.Entry<String, RegisterMemoryDetails> entry : nameToRegisterMemoryDetails.entrySet())
      {
        if (entry.getKey().equals(registerName)) {
          entry.getValue().setObject(value);
        }
      }
      setIsEmpty(registerName, false);
      setIsReady(registerName, true);
    }

    private static boolean isReady(String registerName) throws UnavailbleRegisterException, NoSuchRegisterFoundException {
        Boolean isReady = readyFlags.get(registerName);
        if (isReady == null) {
            throw new NoSuchRegisterFoundException("Flag for register " + registerName + " Not found.");
        } else {
            return isReady.booleanValue();
        }
    }

    private static boolean isEmpty(String registerName) throws NoSuchRegisterFoundException {
        Boolean isEmpty = emptyFlags.get(registerName);
        if (isEmpty == null) {
            throw new NoSuchRegisterFoundException("Flag for register " + registerName + " Not found.");
        } else {
            return isEmpty.booleanValue();
        }
    }

    private static void setIsReady(String registerName, boolean ready) {
        readyFlags.replace(registerName, ready);
    }

    private static void setIsEmpty(String registerName, boolean empty) {
        emptyFlags.replace(registerName, empty);
    }
}
