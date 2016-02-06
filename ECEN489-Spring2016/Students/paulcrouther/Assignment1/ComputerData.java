/**
 * Created by paulc on 1/26/16.
 */
import java.util.Date;
//import java.time.LocalTime;

public class ComputerData {
    public static void main(String args[]) {
        ComputerData data = new ComputerData();
        Date time = new Date();
        String vers = Runtime.class.getPackage().getImplementationVersion();
        String os = System.getProperty("os.name");

        System.out.println(vers + "\n" + os + "\n" + time );

    }

}
