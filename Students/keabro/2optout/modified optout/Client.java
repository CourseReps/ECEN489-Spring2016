
import javax.swing.*;
import java.io.*;
import java.net.*;
public class Client {
    public static void main (String argv[]) throws Exception {
        try{
        String serverAddress = JOptionPane.showInputDialog("enter ip address");
        Socket client = new Socket (serverAddress , 2000);
        ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream is = new ObjectInputStream(client.getInputStream());
        String recv = "";
        do{
        recv = (String) is.readObject();
        System.out.print("Received: " + recv + "\n");
        }while(!recv.equals("exit"));
        is.close();
        os.close();
        client.close();
        
        }catch(Exception e){
            System.out.println(e);
        }
    }
}