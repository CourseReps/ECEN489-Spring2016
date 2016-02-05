/**
 * Created by YYX on 1/30/16.
 * ECEN689-610:SPTP: DATA ACQ EMBEDDED SYS
 * Auther:  Yanxiang Yang
 * Assignment2: OPT-OUT
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    public static final int TCP_PORT = 2000;
    public static final int MAX_CLINENT = 1;
    private ServerSocket server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public void runServer(){
        try{
            server = new ServerSocket(TCP_PORT, MAX_CLINENT);
            while(true){
                try{
                    waitForConnection();
                    getStreams();
                    processConnection();
                }
                catch(EOFException eofException){
                    System.out.println("Server terminated connection");
                }
                finally{
                    closeConnection();
                }
            }
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public void waitForConnection() throws IOException{
        System.out.println("waiting for connection");
        connection = server.accept();
        System.out.println("Connection received from: " + connection.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        input = new ObjectInputStream(connection.getInputStream());
        System.out.println("Got I/O streams");
    }
    private void processConnection() throws IOException{
        System.out.println("Connection Successful");
        try {
            String clientData = (String) input.readObject();
            if (clientData.equals("get_data")){
                // clientData = (String) input.readObject();
                System.out.println("Connection Successful");
                ComputerData computerData = new ComputerData();
                output.writeObject(computerData);
            }
        }
        catch (ClassNotFoundException classNotFoundException){
            System.out.println("\nUnknown object type received");
        }
    }
    private void closeConnection() throws IOException {
        input.close();
        output.close();
        connection.close();
    }



}