import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 * Created by Sam on 4/1/2016.
 */
public class Server extends Thread {

    private ServerSocket server;
    public static boolean running = true;

    public Server(int port) throws IOException{
        server = new ServerSocket(port);
        server.setSoTimeout(10000);
    }

    public void run(){
        while(running){
            try {
                System.out.print("listening on port " + server.getLocalPort() + "...");
                Socket client = server.accept();
                System.out.println("connected to " + client.getRemoteSocketAddress());
                DataInputStream inputStream = new DataInputStream(client.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());

                outputStream.writeUTF("connected");
                client.close();
            }catch(SocketTimeoutException ste){
                System.out.println("Socket timed out");
            }catch(IOException ioe){
                ioe.printStackTrace();
                break;
            }
        }
        System.out.println("socket closed");
    }


    public static void main(String [] args){
        int port = 8080;//Integer.parseInt(args[0]);
        try{
            Thread serverThread = new Server(port);;
            serverThread.start();

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}



