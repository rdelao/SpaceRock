package Network;

import Commands.IncomingListener;

import java.io.IOException;

/**
 created: 2017.02.14 by David R.
 <p>
 Simple test of the Network interfaces: "Does it run",  more or less.
 */
public class TestNetwork {

    public static void main(String[] args) throws IOException {
        DummySat sat = new DummySat();
        sat.start();

        Connection conn = new Connection();
        conn.addIncomingListener(IncomingListener.simplePrinter);
        conn.connectToDummySat();

    }
}
