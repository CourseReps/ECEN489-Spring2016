import java.awt.*;
import java.io.InterruptedIOException;

public class CursorThread extends Thread{
    private Thread cursorThread;

    public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth();
    public double height = screenSize.getHeight();
    public int currentPosX = (int)Math.round(width/2);
    public int currentPosY = (int)Math.round(height/2);

    public void run() {
        try {
            Robot robot = new Robot();

            while(UDPServer.running) {
                if(UDPServer.messageReceived) {
                    currentPosX = currentPosX + (30 * UDPServer.xDegrees / 90);
                    currentPosY = currentPosY + (40 * UDPServer.yDegrees / 90);

                    /* constrain cursor position to screen dimensions */
                    if(currentPosX > width){currentPosX = (int)width;}
                    if(currentPosX < 0){currentPosX = 0;}
                    if(currentPosY > height){currentPosY = (int)height;}
                    if(currentPosY < 0){currentPosX = 0;}

                    robot.mouseMove(currentPosX, currentPosY);

                }
                Thread.sleep(50);
            }

        } catch (AWTException|InterruptedException e) {
            System.out.println("CursorThread stopped");
            UDPServer.running = false;
        }
    }

    public void start () {
        if (cursorThread == null) {
            cursorThread = new Thread (this);
            cursorThread.start ();
        }
    }

}