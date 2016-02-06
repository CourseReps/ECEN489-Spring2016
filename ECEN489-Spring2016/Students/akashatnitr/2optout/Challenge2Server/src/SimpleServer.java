
import java.io.*;
import java.net.*;


public class SimpleServer {
	public static int portNo = 9999;
       public static void main(String[] args) throws Exception {
        System.out.println("The computer data server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(portNo);
        try {
            while (true) {
                new SServer(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

      private static class SServer extends Thread {
        private Socket socket;
        private int clientNumber;
        ComputerData cdata = new ComputerData();
        public SServer(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("connected client  " + clientNumber + " at " + socket);
        }

         public void run() {
        	 
            try {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                   	out.println("welcome to simple server.... Input x or blank to quit");
                	String cdataserver = cdata.toString();
                	out.println(cdataserver);
                    
                    while (true) {
                        String input = in.readLine();
                        if (input == null || input.equals("x")) {
                            break;
                        }
                        out.println("message from server:"+input);
                    }
                    
                
            } catch (IOException e) {
            	System.out.println(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                	System.out.println(e);
                }
                System.out.println("client " + clientNumber + " closed");
            }
        }

         
    }
}