
import javax.swing.*;
import java.io.*;
import java.net.*;
public class client {
    public static void main (String argv[]) throws Exception {
        try{
        String serverAddress = JOptionPane.showInputDialog();
        Socket client = new Socket (serverAddress , 2000);
        ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream is = new ObjectInputStream(client.getInputStream());
        while(recv != "exit"){
        String recv = is.readObject();
        System.out.println("Received: \n" + recv);
    }
        is.close();
        os.close();
        client.close();
        
        }catch(Exception e){
            System.out.println(e);
        }
    }
}