import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/** ServerThread1. Runs on machine 2 and receives packets from UDPServerThread running on machine 1. 
 *  It then decodes these messages and stores them for the cursor thread to use.
 */
public class ServerThread1 extends Thread{

    protected static DatagramSocket socket = null;

    public ServerThread1() throws IOException{
        this("UDPServerThread");
    }

    public ServerThread1(String name) throws IOException{
        super(name);
        socket = new DatagramSocket(8081); // create socket
        socket.setSoTimeout(90000); // set timeout to 1.5 min
    }

    public void run(){

        try {
            System.out.println("server running at " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("press ENTER to stop");
        }catch(UnknownHostException e){
            System.out.println("cannot connect to network");
        }
        List<Integer> received = new ArrayList<Integer>();
        while(UDPServer.running){

            try {
                byte[] buf = new byte[256];

                // receive data
                UDPServer.messageReceived = false;
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                UDPServer.messageReceived = true; // set received flag to true to trigger cursor movement

                received = decodeByteMessage(packet.getData());

                UDPServer.yDegrees = -1*received.get(0);
                UDPServer.xDegrees = -1*received.get(1);

                //System.out.println("(2) x: " + UDPServer.xDegrees + " y: " + UDPServer.yDegrees);


                /* rotation threshold */
                if(Math.abs(UDPServer.xDegrees)<5){UDPServer.xDegrees = 0;} // cursor won't move horizontally if it is within five degrees of 0
                else if(UDPServer.xDegrees<-5){UDPServer.xDegrees = UDPServer.xDegrees + 5;}
                else{UDPServer.xDegrees = UDPServer.xDegrees - 5;}
                if(Math.abs(UDPServer.yDegrees)<5){UDPServer.yDegrees = 0;} // cursor won't move horizontally if it is within five degrees of 0
                else if(UDPServer.yDegrees<-5){UDPServer.yDegrees = UDPServer.yDegrees + 5;}
                else{UDPServer.yDegrees = UDPServer.yDegrees - 5;}

                sleep(100/UDPServer.userCount);
            }catch(IOException|InterruptedException e){
                socket.close();
                UDPServer.running = false;
            }
        }
        socket.close();
        System.out.println("\nserver stopped");
        if(UDPServer.flag){
            System.out.println("press ENTER to finish");
        }
    }


    public static List<Integer> decodeByteMessage(byte[] b) {
        List<Integer> list = new ArrayList<Integer>();

        int i1 = b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;

        int i2 = b[7] & 0xFF |
                (b[6] & 0xFF) << 8 |
                (b[5] & 0xFF) << 16 |
                (b[4] & 0xFF) << 24;

        list.add(i1);
        list.add(i2);

        return list;
    }

}
