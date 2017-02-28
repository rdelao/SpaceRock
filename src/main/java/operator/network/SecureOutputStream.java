package operator.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 created by David R. 02/14/2016
 <p>
 This class mocks the writable side of the encrypted channel needed for communication between
 the SpaceRock satellite and operator station.  Currently just delegates to an internal
 ObjectOutputStream.
 */
public class SecureOutputStream {

    private final ObjectOutputStream out;


    public SecureOutputStream(OutputStream outputStream) throws IOException {
        out = new ObjectOutputStream(outputStream);
    }


    /**
     Write an object out on this stream.  This method has the same semantics as
     ObjectOutputStream.writeObject()

     @param obj Object to write

     @throws IOException in any instance ObjectOutputStream.writeObject does.
     */
    public void writeObject(Object obj) throws IOException {
        out.writeObject(obj);
    }


}
