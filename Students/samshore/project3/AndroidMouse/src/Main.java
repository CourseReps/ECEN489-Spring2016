import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

class Main implements Runnable {
    private Thread mouseThread;
    public static char input;
    public static boolean running = false;

    public void run() {
        try {
            Robot robot = new Robot();
            Random random = new Random();

            while (input != 'q') {
                robot.mouseMove(random.nextInt(1600), random.nextInt(900));
                running = true;
            }
        } catch (AWTException e) {
            System.out.println("interrupted.");
            running = false;
        }
        System.out.println("done");
        running = false;
    }

    public void start ()
    {
        if (mouseThread == null)
        {
            mouseThread = new Thread (this);
            mouseThread.start ();
        }
    }

}



class TestThread {
    public static void main(String args[]) {

        Main R1 = new Main();
        R1.start();

    }
}
