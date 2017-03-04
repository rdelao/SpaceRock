package fpga.memory;

import java.io.*;
import java.util.Arrays;

/**
 * Created by T10 on 3/3/2017.
 *
 * Represents memory for FPGA board. This memory contains
 * the control registers. The memory is composed of a byte
 * array. This may change to int array if Roman really wants
 * that.
 */
public class Memory {

    //simulated storage
    private static byte[] storage;

    //called to allot storage size
    public static void initMemory(int numBytes)
    {
        storage = new byte[numBytes];
    }

    /*
    * Given a register insert it into the simulated memory by
    * serializing it. In order to insert serailized object
    * must first scan memory and find large enough opening somewhere.
    */
    public static void insertIntoMemory(RegisterBase reg)
    {
        try
        {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(reg);
            objectOutputStream.flush();
            objectOutputStream.close();
            int size = byteOutputStream.size();
            int start = scanMemoryForOpening(size);
            insertIntoMemoryAtLocation(start, byteOutputStream.toByteArray());
        } catch (IOException e){
            //implement this
        }
    }

    /*
        Use register details to find register in memory.  Next deserialize it and return
        the register object.
     */
    public static RegisterBase retrieveFromMemory(RegisterMemoryDetails regDetails)
    {
        try {
            byte[] objectBytes = new byte[regDetails.getLength()];
            int endByteLocation = regDetails.getStartPosition() + regDetails.getLength();
            objectBytes = Arrays.copyOfRange(storage, regDetails.getStartPosition(), endByteLocation);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectBytes);
            ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);

            Object obj = objectInput.readObject();
            RegisterBase retrievedRegister = (RegisterBase) obj;
            return retrievedRegister;
        } catch (IOException e)
        {
            //implement this
        } catch (ClassNotFoundException e)
        {
            //implement this
        }
        return null;
    }

    /*
    * Find first segment of memory large enough to store.
    * Return start position of this segment
    */
    private static int scanMemoryForOpening(int size)
    {
        return 0;
    }

    /*
    * Given a start klocation, copy objectToWrite into memory starting at
    * start.
     */
    private static void insertIntoMemoryAtLocation(int start, byte[] objectToWrite)
    {
        int length = objectToWrite.length;
        for (int i=0; i<length; i++)
        {
            storage[i+start]=objectToWrite[i];
        }
    }



}
