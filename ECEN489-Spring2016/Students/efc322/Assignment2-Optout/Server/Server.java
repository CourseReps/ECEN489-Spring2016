import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Fanchao Zhou on 1/31/2016.
 */
public class Server {
    private static final String cmdServerData = "get_data";

    public static void main(String[] args){
        Scanner kbInput = new Scanner(System.in);
        System.out.print("Please Type In the handshake PORT NUMBER: ");
        final short portNum = kbInput.nextShort();
        System.out.print("Please Type In the MAX NUMBER OF CLIENTS allowed to wait in queue: ");
        final short maxClients = kbInput.nextShort();
        kbInput.close();

        try {
            ServerSocket server = new ServerSocket(portNum, maxClients);                          //Set up a server
            while(true){
                System.out.println("Waiting to be Connected......");
                Socket client = server.accept();                                                  //Waiting to be connected
                System.out.println("Connected By " + client.getInetAddress());
                ObjectOutputStream outputToNet = new ObjectOutputStream(client.getOutputStream());//Get Ready to send data
                ObjectInputStream inputFromNet = new ObjectInputStream(client.getInputStream());  //Get Ready to receive commands
                String clientCmd;

                clientCmd = (String)inputFromNet.readObject();                                    //Read the command from the client
                System.out.println("Command: " + clientCmd);
                if(clientCmd.equals(cmdServerData)){                                              //Processing the Data Request
                    System.out.println("Data Request From " + client.getInetAddress());
                    ComputerData serverData = new ComputerData();
                    outputToNet.writeObject(serverData);

                    /*Release all the resources allocated for the current client*/
                    inputFromNet.close();
                    outputToNet.close();
                    client.close();
                }else{                                                                            //Command Unknown
                    /*Release all the resources allocated for the current client*/
                    inputFromNet.close();
                    outputToNet.close();
                    client.close();
                }
            }
        }catch(Exception e){
            System.err.print(e);
        }

    }
}
