/**
 * Created by paulc on 1/30/16.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.*;

/**
 * Trivial client for the date server.
 */
public class DateClient {

    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    public static void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog(
                "Enter IP Address of a machine that is\n" +
                        "running the date service on port 2000:");
        Socket s = new Socket(serverAddress, 2000);
        String getData = "get_data";
        BufferedReader input =
                new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        Vector v = new Vector();
        while ((answer = input.readLine()) != null) {
            v.add(answer);
            System.out.println(answer);
            //JOptionPane.showMessageDialog(null, answer);
        }
        //System.out.print(answer);
        //for(int i=0; i< v.size(); i++){
            //System.out.println(v.get(i));
       // }
        JOptionPane.showMessageDialog(null,new JList(v));
        System.exit(0);
    }
}
