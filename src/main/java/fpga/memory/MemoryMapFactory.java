package fpga.memory;

import com.sun.org.apache.xpath.internal.operations.Bool;
import fpga.objectdetection.Debris;
import sensor.ZoomLevel;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Class used to initialize MemoryMap information from the control register configuration file
 */
public final class MemoryMapFactory
{

  /**
   * Load the register map from the control register configuration file
   * @return Hashmap with name of register as key and register details as value
   * @throws MemoryInitializationException
   */
    public static HashMap<String, RegisterMemoryDetails> loadRegisterMap()
            throws MemoryInitializationException {
        HashMap<String, RegisterMemoryDetails> registerMap = new HashMap<>();
        try {
          ClassLoader classloader = Thread.currentThread().getContextClassLoader();
          InputStream is = classloader.getResourceAsStream("fpga/control_registers.csv");
          final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
          String line = null;
          while ((line = reader.readLine()) != null) {
            String[] csvAsArray = line.split(",");
            String name = csvAsArray[0];
            int typeId = Integer.parseInt(csvAsArray[1]);
            registerMap.put(name, initReg(typeId));
          }
        } catch (NullPointerException|IOException exception) {
            throw new MemoryInitializationException(
              "Resource file containing control register mappings does not exist.");
        }
        return registerMap;
    }

    /**
     * Initializes a generic RegisterMemoryDetails object with the type correstponsing to the typeId
     * @param typeId Integer defining type of register
     * @param <T> Generic type of RegisterMemoryDetails
     * @return RegisterMemoryDetails of type T
     * @throws MemoryInitializationException
     */
    private static <T> RegisterMemoryDetails<T> initReg(int typeId) throws MemoryInitializationException {
        if (typeId == 0) {
          return (RegisterMemoryDetails<T>) new RegisterMemoryDetails<Boolean>(null);
        }
        else if (typeId == 1) {
          return (RegisterMemoryDetails<T>) new RegisterMemoryDetails<Integer>(null);
        }
        else if (typeId == 2) {
          return (RegisterMemoryDetails<T>) new RegisterMemoryDetails<ZoomLevel>(null);
        }
        else if (typeId == 3) {
          return (RegisterMemoryDetails<T>) new RegisterMemoryDetails<BufferedImage>(null);
        }
        else if (typeId == 4) {
          return (RegisterMemoryDetails<T>) new RegisterMemoryDetails<List<Debris>>(null);
        } else {
          throw new MemoryInitializationException("Invalid typeId found for register.");
        }
    }
}
