package Network;

import Commands.*;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;


/**
 The Connection class encapsulates the operator's network interface.  It maintains a list of
 listeners for incoming data and notifies them on receipt.
 <p>
 created by David R. 02/12/2017
 */
public class Connection {


    /** Default timeout for connecting to satellite, in milliseconds */
    public static final int CONN_TIMEOUT = 2000;
    private final List<IncomingListener> incomingListeners = new ArrayList<>();
    private Socket sock = new Socket();
    private SecureOutputStream outStream;


    /**
     Connect to the local (dummy) Satellite for testing

     @throws IOException if an error occurs during the connection.  See {@code Socket.connect()}
     */
    public void connectToDummySat() throws IOException {
        connectToSat(InetAddress.getLocalHost());
    }


    /**
     Connect to the satellite at the specified address with the default port and timeout. This starts
     a new thread in which incoming data is handled by this Connection.  Install a listener to
     respond to incoming data from the satellite.

     @param inetAddr INetAddress of the satellite

     @throws IOException if an error occurs during the connection.  See {@code Socket.connect()}
     */
    public void connectToSat(InetAddress inetAddr) throws IOException {
        connectToSat(new InetSocketAddress(inetAddr, DummySat.SAT_PORT), CONN_TIMEOUT);
        listen();
    }


    /**
     Terminate the connection with the satellite.
     */
    public void disconnect() {
        try {
            sock.close();
        } catch (IOException e) {
            /* Doing nothing here is probably fine for now. */
            /* TODO: Better exception handling */
        }
    }


    /*
    In a new thread, start reading from the socket's output stream.  These reads block, hence the
     new thread.  IncomingListeners can be added to subscribe to any kind of input received.
     */
    private void listen() {
        Runnable listener = () ->
        {
            try {
                SecureInputStream in = new SecureInputStream(sock.getInputStream());

                /* for now, if we get an exception, simply exit the loop.  If/when we have better
                 exception handling, we could lift the loop out and break only when strictly
                 necessary. */
                while (true) {
                    System.out.println("Connection.listen: reading object");
                    Object data = in.readObject();
                    System.out.println("Connection.listen: read object: " + data.toString());
                    if (data instanceof IncomingData) {
                        IncomingData incoming = (IncomingData) data;
                        synchronized (incomingListeners) {
                            incomingListeners
                                    .forEach(l -> l.newAsteroidData(incoming.asteroids,
                                                                    incoming.timestamp));
                        }
                    } else if (data instanceof IncomingImage) {
                        IncomingImage incoming = (IncomingImage) data;
                        synchronized (incomingListeners) {
                            incomingListeners
                                    .forEach(l -> l.newImageData(incoming.getImage(),
                                                                 incoming.id));
                        }
                    } else {
                        /* This shouldn't happen.
                        TODO: Respond gracefully if we get anything not in the Incoming* classes.
                         */
                    }
                }
            } catch (EOFException e) {
                        /* Socket closed, quietly continue */
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        };

        new Thread(listener).start();
    }


    /**
     Send a new camera spec to the Satellite.

     @param spec Spec to send

     @throws IOException for any reason that ObjectOutputStream.writeObject() does
     */
    public void sendCameraSpec(OutgoingCameraSpec spec) throws IOException {
        outStream.writeObject(spec);
    }


    /**
     Send a new camera spec to the satellite.

     @param zoom new Zoom level
     @param width new width
     @param height new height
     @param onOff whether camera should be on or off

     @throws IOException for any reason that ObjectOutputStream.writeObject() does
     */
    public void sendCameraSpec(double zoom, int height, int width, boolean onOff)
            throws IOException {
        sendCameraSpec(new OutgoingCameraSpec(zoom, height, width, onOff));
    }


    /**
     Send a request for an image chunk to the satellite.

     @param req Request to send

     @throws IOException for any reason that ObjectOutputStream.writeObject() does
     */
    public void sendImageRequest(OutgoingImageRequest req) throws IOException {
        outStream.writeObject(req);
    }


    /**
     Send a request for an image chunk to the satellite given an asteroid ID

     @param id (Satellite-side) id of the asteroid whose image chunk is desired

     @throws IOException for any reason that ObjectOutputStream.writeObject() does
     */
    public void sendImageRequest(long id) throws IOException {
        sendImageRequest(new OutgoingImageRequest(id));
    }


    /**
     Connect to a Satellite given a SocketAddress and desired timeout

     @param sockAddr Address to connect to
     @param timeout How long to keep trying

     @throws IOException For any reason that Socket.connect() does.
     */
    public void connectToSat(SocketAddress sockAddr, int timeout)
            throws IOException {
        sock.connect(sockAddr, timeout);
        outStream = new SecureOutputStream(sock.getOutputStream());
    }


    /**
     Add a Listener to respond to incoming data from the Satellite.

     @param listener IncomingListener to add.
     */
    public void addIncomingListener(IncomingListener listener) {
        synchronized (incomingListeners) {
            incomingListeners.add(listener);
        }
    }


    /**
     Remove a listener from the list of IncomingListeners (using List.remove())

     @param listener listener to remove
     */
    public void removeIncomingListener(IncomingListener listener) {
        synchronized (incomingListeners) {
            incomingListeners.remove(listener);
        }
    }

}
