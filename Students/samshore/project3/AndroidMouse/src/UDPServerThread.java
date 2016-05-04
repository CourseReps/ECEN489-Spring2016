import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/** UPServerThread class. This class opens sockets and receives packets from Android devices.
 *  The IP address of the packet is checked. If it is a new IP, a new user object is created, assigned
 *  a team, and added to the users list. If the user is assigned to the other team, the data packet will be sent
 *  to the secondary server. Otherwise, the packet is decoded and the data is used to move the mouse cursor.
 */
public class UDPServerThread extends Thread{

    protected static DatagramSocket socket1 = null;
    protected static DatagramSocket socket2 = null;
    private List<Users> userList = new ArrayList<Users>();


    public UDPServerThread() throws IOException{
        this("UDPServerThread");
    }

    public UDPServerThread(String name) throws IOException{
        super(name);
        socket1 = new DatagramSocket(8080); // socket to receive packets
        socket1.setSoTimeout(90000); // set timeout to 1.5 min
        socket2 = new DatagramSocket(); // socket to send packets
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
        List<Integer> received = new ArrayList<Integer>(); // create a list to store the received data
        while(UDPServer.running){

            try {
                byte[] buf = new byte[256];

                /* receive data */
                UDPServer.messageReceived = false; // flag signifies a received message, when set to true triggers a mouse movement
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket1.receive(packet); // receive packet
                UDPServer.messageReceived = true;

                /* determine where the packet came from */
                currentIp = packet.getAddress().toString();

                /* check if the device has been assigned a team */
                userExists = false;
                for(Users user:userList){ // iterate over all users in user list
                    if(currentIp.equals(user.ip)){ // check packet ip against user ips
                        userExists = true;
                        currentTeam = user.team; // get users team
                    }
                }

                /* if ip isn't on the list, assign team and add to users list */
                if(!userExists){
                    currentTeam = userCount%2; // assign team based on how many users are connected
                    Users user = new Users(currentIp, currentTeam); // create new user object
                    userList.add(user); // add user to user list
                    System.out.println("user " + user.ip + " joined team " + (user.team+1) + "!");
                    userCount++;
                }

                received = decodeByteMessage(packet.getData()); // decode received message

                if(currentTeam == 1){   // team 1 (machine 2)
                    /* send data to other machine */
                    byte[] data = packet.getData();
                    InetAddress address = InetAddress.getByName(UDPServer.secondaryIP);
                    address = InetAddress.getByName(address.getHostAddress());
                    DatagramPacket newPacket = new DatagramPacket(data, data.length, address, 8081);
                    socket2.send(newPacket);

                }
                else{   // team 0 (machine 1)

                    /* adjust sign to make movements match rotation */
                    UDPServer.yDegrees = -1 * received.get(0);
                    UDPServer.xDegrees = -1 * received.get(1);

                    //System.out.println("(1) x: " + UDPServer.xDegrees + " y: " + UDPServer.yDegrees);


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

                    sleep(50/UDPServer.userCount);
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

    /** decodeByteMessage. Takes a byte message and returns a list of two integer values
     * (pitch and roll)
     */
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
