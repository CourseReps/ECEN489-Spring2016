/**
 * Created by Sam on 4/8/2016.
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class DataClient {
    public static void main(String[] args) throws IOException{

        System.out.println("client running...");

        // get a datagram socket
        DatagramSocket socket = new DatagramSocket();
        int message = 0;

        while(true) {
            // send data
            byte[] buf = intToByteArray(message);
            InetAddress address = InetAddress.getByName("Sam-PC");
            address = InetAddress.getByName(address.getHostAddress());
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8080);
            socket.send(packet);
            message++;
        }
    }

    public static int byteArrayToInt(byte[] b)
    {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a)
    {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
}
