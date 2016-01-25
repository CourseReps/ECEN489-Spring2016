/**
 * Created by keatonbrown on 1/25/16.
 */
public class ComputerData {

    public static void main(String[] args)
    {
        System.out.println("Java Version" + " " + System.getProperty("java.version"));
        System.out.println("OS Version" + " " + System.getProperty("os.name")+ " " + System.getProperty("os.version"));
        System.out.println("Current Time" + " " + java.time.LocalTime.now());
    }
}
