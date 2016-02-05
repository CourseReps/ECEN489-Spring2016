

import java.io.*;
import java.net.*;
import java.util.Scanner;
   
public class JStreamClient {

    private BufferedReader bIn;
    private PrintWriter bOut;
    
    
     

    
    public void connectToServer() throws IOException { 
        String serverAddress ="127.0.0.1";
        int portNo = 9999;         
        Socket socket = new Socket(serverAddress,portNo);
        bIn = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        bOut = new PrintWriter(socket.getOutputStream(), true);

        
        for (int i = 0; i < 10; i++) {
          
            System.out.println(bIn.readLine());
        }
        System.out.println("Enter message for server. Enter x to quit");
        
        String inputText;
        Scanner sc = new Scanner(System.in);
        inputText=sc.next();
        while(inputText.equals("x")==false){
             bOut.println(inputText);
            
             String response;
             try {
                 response = bIn.readLine();
                 if (response == null || response.equals("")) {
                       System.exit(0);
                   }
             } catch (IOException ex) {
                    response = "Error: " + ex;
             }
            
             System.out.println(response);
             
             inputText=sc.next();
             
             
        }
        
        
    }

     
    public static void main(String[] args) throws Exception {
        JStreamClient client = new JStreamClient();
        client.connectToServer();
    }
}