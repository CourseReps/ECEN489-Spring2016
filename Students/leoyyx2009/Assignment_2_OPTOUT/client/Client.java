/**
 * Created by YYX on 1/30/16.
 * ECEN689-610:SPTP: DATA ACQ EMBEDDED SYS
 * Auther:  Yanxiang Yang
 * Assignment2: OPT-OUT
 */

import java.io.*;
import java.net.Socket;
import java.net.InetAddress;

public class Client {
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String message;
    public void runClient(){
        try {
            connectToServer();
            getStreams();
            processConnection();
        }
        catch(EOFException eofException){
            System.out.println("Client termincated connection");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            closeConnection();
        }
    }
    private void connectToServer() throws IOException {

        System.out.println("Attempting connection");
        InetAddress host = InetAddress.getByName("127.0.0.1");
        client = new Socket(host, 2000);
        System.out.println("Connected to: " + client.getInetAddress().getHostName());
    }
    private void getStreams() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
        output.writeObject("get_data");
        System.out.println("Got I/O streams");
    }
    private void processConnection() throws IOException{
        try{
            ComputerData data = (ComputerData)input.readObject();
            System.out.println("received" + data.toString());
        }
        catch (ClassNotFoundException classNotFoundException) {
            System.out.println("Unknown object type received");
        }
    }
    private void closeConnection(){
        System.out.println("closing connection");
        try{
            output.close();
            input.close();
            client.close();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
