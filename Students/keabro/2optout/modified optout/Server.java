import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(2000, 15);
            while (true) {
                Socket socket = server.accept();
                String input = "";
                try {
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                do{
                /**Scanner reader = new Scanner(System.in;  // Reading from System.in
                System.out.print("Enter a message: ");
                input = reader.next();**/
                input = System.console().readLine("Message:");
                os.writeObject(input);
                }while(!input.equals("exit"));
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


