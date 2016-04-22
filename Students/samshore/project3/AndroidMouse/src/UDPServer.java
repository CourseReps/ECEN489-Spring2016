/**
 * Created by Sam on 4/8/2016.
 */

import java.io.*;

public class UDPServer {
    public static boolean running;
    public static int xDegrees;
    public static int yDegrees;
    public static boolean messageReceived = false;

    public static void main(String[] args) throws IOException{
        running = true;
        new UDPServerThread().start();
        new CursorThread().start();

    }
}
