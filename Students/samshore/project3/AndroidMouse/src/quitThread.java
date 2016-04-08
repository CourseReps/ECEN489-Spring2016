
import java.util.Scanner;

/**
 * Created by Sam on 4/1/2016.
 */
public class quitThread extends Thread {

    public void run() {
        Scanner reader = new Scanner(System.in);
        String input;
        while (Server.running) {
                input = reader.next();
                if (!(input.equals(" "))) {
                    Server.running = false;
                }
        }
    }
}




