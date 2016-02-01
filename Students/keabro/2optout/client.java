
import javax.swing.*;
import java.io.*;
import java.net.*;
public class client {
    public static void main (String argv[]) throws Exception {
        try{
        String serverAddress = JOptionPane.showInputDialog("Enter IP Address of Server on port 4444");
        Socket client = new Socket (serverAddress , 2000);
        ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
        String req = "get_data";
        os.writeObject(req);
        ObjectInputStream is = new ObjectInputStream(client.getInputStream());
        ComputerData recv = (ComputerData) is.readObject();
        System.out.println("Received: \n" + recv);
        is.close();
        os.close();
        client.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}