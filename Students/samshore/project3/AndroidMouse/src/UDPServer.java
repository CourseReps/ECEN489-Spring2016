/**
 * Created by Sam on 4/8/2016.
 */

import java.io.*;
import java.util.Scanner;

public class UDPServer {
    public static boolean running;
    public static int xDegrees;
    public static int yDegrees;
    public static boolean messageReceived = false;
    public static boolean flag = true;
    public static String secondaryIP;
    public static int userCount;

    public static void main(String[] args) throws IOException{
        running = true;
        Scanner scan1 = new Scanner(System.in);
        Scanner scan2 = new Scanner(System.in);
        Scanner scan3 = new Scanner(System.in);
        Scanner scan4 = new Scanner(System.in);


        System.out.println("Machine 1 or 2?");
        int n = scan1.nextInt();

        System.out.println("Number of expected users?");
        userCount = scan4.nextInt();


        if(n==1) {
            System.out.println("Enter IP of second machine: ");
            secondaryIP = scan2.nextLine();
            UDPServerThread serverThread = new UDPServerThread();
            serverThread.start();
        }
        else{
            ServerThread1 serverThread = new ServerThread1();
            serverThread.start();
        }

        CursorThread cursorThread = new CursorThread();
        cursorThread.start();

        scan3.nextLine();

        if(n==1){
            UDPServerThread.socket1.close();
            UDPServerThread.socket2.close();
        }
        else{ServerThread1.socket.close();}
        flag = false;

    }
}
