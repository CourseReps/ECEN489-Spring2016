/**
 * Created by Sam on 1/25/2016.
 */
import java.util.Date;

public class ComputerData {
    public static void main(String[] args){
        // Display the date
        Date date = new Date();
        System.out.println(date.toString());

        // Display the Java version
        System.out.print("Java version ");
        System.out.println(System.getProperty("java.version"));

        // Display OS
        System.out.print("running on ");
        System.out.println(System.getProperty("os.name"));
    }
}
