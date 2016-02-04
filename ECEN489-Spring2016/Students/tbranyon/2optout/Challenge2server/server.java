import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tbranyon on 1/27/16.
 */
public class server
{
    public static void main(String[] args)
    {
        short portnumber = 2000;
        short maxclients = 1;
        while(true)
        {
            try{
                ServerSocket server = new ServerSocket(portnumber, maxclients);
                Socket connection = server.accept(); //wait for connect
                ObjectOutputStream os = new ObjectOutputStream(connection.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(connection.getInputStream());
                String cmd = "";
                while(!cmd.equals("get_data"))
                    cmd = (String) is.readObject();
                ComputerData dat = new ComputerData();
                os.writeObject(dat);
                os.close();
                is.close();
                connection.close();
                server.close();
            }catch(Exception e) {
                System.err.print(e);
            }
        }
    }
}
