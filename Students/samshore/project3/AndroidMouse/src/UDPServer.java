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

    public static void main(String[] args) throws IOException{
        running = true;
        UDPServerThread serverThread = new UDPServerThread();
        CursorThread cursorThread = new CursorThread();

        serverThread.start();
        cursorThread.start();

        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        UDPServerThread.socket.close();
        flag = false;

    }



}
