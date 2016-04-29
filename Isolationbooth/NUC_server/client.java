import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * Created by tbranyon on 1/27/16.
 */
public class client
{
    public static void main(String[] args)
    {
        String file = "default";
        try{
            file = args[0];
            System.out.println(file);
        }catch(Exception e){
            System.err.println("Argument" + args[0] + " must be an String.");
            System.exit(1);
        }
        try{
            String addr_str = "10.202.103.231"; //JOptionPane.showInputDialog("Enter IP");
            InetAddress addr = InetAddress.getByName(addr_str);
            Socket connection = new Socket(addr, 2000);
            ObjectOutputStream os = new ObjectOutputStream(connection.getOutputStream());
            String req = "get_pic";
            os.writeObject(req);
            ObjectInputStream is = new ObjectInputStream(connection.getInputStream());
            int length = is.readInt();
            System.out.println("Incoming length: " + length);
            byte[] pic = new byte[0];
            if(length>0) {
                pic = new byte[length];
                System.out.println("pic.length = " + pic.length);
                is.readFully(pic, 0, pic.length); // read the picture
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(pic);
            fos.close();
            is.close();
            os.close();
            Thread.sleep(5000);
            connection.close();
            return;
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
