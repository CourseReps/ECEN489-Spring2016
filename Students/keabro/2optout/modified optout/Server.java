import java.io.*;
import java.net.*;
import javax.swing.*;
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(2000, 15);
            while (true) {
                Socket socket = server.accept();
                try {
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                while(input != "exit"){
                String input = JOptionPane.showInputDialog();
                os.writeObject(input);
                }
                os.close();
                is.close();
                socket.close();
                server.close();
                }catch(Exception e) {
                System.err.print(e);
            }
        }
    }
}


