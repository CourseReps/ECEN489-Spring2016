import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Fanchao Zhou on 1/31/2016.
 */
public class Client {
    private static String cmdServerData = "get_data";

    public static void display(final ComputerData serverData){
        System.out.println(
                "Server Time   : " + serverData.getTime() + "\n" +
                "Classpath     : " + serverData.getClassPath() + "\n" +
                "JRE Vendor    : " + serverData.getJavaVender() + "\n" +
                "JRE Version   : " + serverData.getJavaVer() + "\n" +
                "OS Arch       : " + serverData.getOSArch() + "\n" +
                "OS Name       : " + serverData.getOSName() + "\n" +
                "OS Version    : " + serverData.getOSVer() + "\n" +
                "User Home Dir : " + serverData.getUserHome() + "\n" +
                "Username      : " + serverData.getUserName()
        );
    }

    public static void main(String[] args){
        Scanner kbInput = new Scanner(System.in);
        System.out.print("Please Type In the Server's IP Address: ");
        final String serverIPString = kbInput.nextLine();
        System.out.print("Please Type In the Server's Port Number to connect to: ");
        final short portNum = kbInput.nextShort();
        kbInput.close();

        try{
            System.out.println("Connection to server......");
            Socket server = new Socket(InetAddress.getByName(serverIPString), portNum);       //Connect to the server
            ObjectInputStream inputFromNet = new ObjectInputStream(server.getInputStream());  //Get Ready to receive commands
            ObjectOutputStream outputToNet = new ObjectOutputStream(server.getOutputStream());//Get Ready to send data

            System.out.printf("Sending Request to server%n");
            outputToNet.writeObject("get_data");
            ComputerData serverData = (ComputerData)inputFromNet.readObject();
            display(serverData);

            inputFromNet.close();
            outputToNet.close();
            server.close();
        }catch(IOException e) {
            System.err.println(e);
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
    }
}
