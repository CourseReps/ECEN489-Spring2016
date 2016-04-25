

/**
 * Created by Sam on 4/8/2016.
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class UDPServerThread extends Thread{

    protected static DatagramSocket socket1 = null;
    protected static DatagramSocket socket2 = null;
    private List<Users> userList = new ArrayList<Users>();


    public UDPServerThread() throws IOException{
        this("UDPServerThread");
    }

    public UDPServerThread(String name) throws IOException{
        super(name);
        socket1 = new DatagramSocket(8080);
        socket1.setSoTimeout(90000);
        socket2 = new DatagramSocket();
    }

    public void run(){
        String currentIp;
        int currentTeam = 0;
        Boolean userExists = false;
        int userCount = 0;

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
                socket1.receive(packet);
                UDPServer.messageReceived = true;

                /* determine where the packet came from */
                currentIp = packet.getAddress().toString();

                /* check if the device has been assigned a team */
                userExists = false;
                for(Users user:userList){
                    if(currentIp.equals(user.ip)){
                        userExists = true;
                        currentTeam = user.team;
                    }
                }

                /* if ip isn't on the list, assign team and add to users list */
                if(!userExists){
                    currentTeam = userCount%2;
                    Users user = new Users(currentIp, currentTeam);
                    userList.add(user);
                    userCount++;
                }

                received = decodeByteMessage(packet.getData());

                if(currentTeam == 1){   // team 1 (machine 2)
                    /* send data to other machine */
                    byte[] data = packet.getData();
                    InetAddress address = InetAddress.getByName(UDPServer.secondaryIP);
                    address = InetAddress.getByName(address.getHostAddress());
                    DatagramPacket newPacket = new DatagramPacket(data, data.length, address, 8081);
                    socket2.send(newPacket);

                }
                else{   // team 0 (machine 1)

                    UDPServer.yDegrees = -1 * received.get(0);
                    UDPServer.xDegrees = -1 * received.get(1);

                    System.out.println("(1) x: " + UDPServer.xDegrees + " y: " + UDPServer.yDegrees);


                     /* rotation threshold */
                    if (Math.abs(UDPServer.xDegrees) < 5) {
                        UDPServer.xDegrees = 0;
                    } // cursor won't move horizontally if it is within five degrees of 0
                    else if (UDPServer.xDegrees < -5) {
                        UDPServer.xDegrees = UDPServer.xDegrees + 5;
                    } else {
                        UDPServer.xDegrees = UDPServer.xDegrees - 5;
                    }
                    if (Math.abs(UDPServer.yDegrees) < 5) {
                        UDPServer.yDegrees = 0;
                    } // cursor won't move horizontally if it is within five degrees of 0
                    else if (UDPServer.yDegrees < -5) {
                        UDPServer.yDegrees = UDPServer.yDegrees + 5;
                    } else {
                        UDPServer.yDegrees = UDPServer.yDegrees - 5;
                    }

                    sleep(50);
                }
            }catch(IOException|InterruptedException e){
                socket1.close();
                socket2.close();
                UDPServer.running = false;
            }
        }
        socket1.close();
        socket2.close();
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
