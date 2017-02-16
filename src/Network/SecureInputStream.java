package Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 created: 2017.02.14 by David R.
 <p>
 This class mocks the secure input side of the communication channel between the SpaceRock
 satellite and the operator base station.  Currently this just delegates to an internal
 ObjectInputStream.
 */
public class SecureInputStream {
    private final ObjectInputStream in;


    public SecureInputStream(InputStream stream) throws IOException {
        in = new ObjectInputStream(stream);
    }


    /**
     Read an Object from this stream.  This method has the same semantics as ObjectInputStream
     .readObject() (notably, it blocks).

     @return Object read fromt the stream

     @throws IOException when ObjectInputStream.readObject() does
     @throws ClassNotFoundException when ObjectInputStream.readObject() does
     */
    public Object readObject() throws IOException, ClassNotFoundException {
        return in.readObject();
    }
}
