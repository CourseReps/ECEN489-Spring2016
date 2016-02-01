/**
 * Created by YYX on 1/31/16.
 */
import javax.swing.JFrame;

public class ServerTest {
        public static void main(String[] args) {
                Server application = new Server();
                application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                application.runServer(); // run server application
        }
}