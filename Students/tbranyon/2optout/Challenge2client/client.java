import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
        try{
            String addr_str = JOptionPane.showInputDialog("Enter IP");
            InetAddress addr = InetAddress.getByName(addr_str);
            Socket connection = new Socket(addr, 2000);
            ObjectOutputStream os = new ObjectOutputStream(connection.getOutputStream());
            String req = "get_data";
            os.writeObject(req);
            ObjectInputStream is = new ObjectInputStream(connection.getInputStream());
            ComputerData recv = (ComputerData) is.readObject();
            System.out.println("Received: \n" + recv);
            is.close();
            os.close();
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
