/**
 * Created by Chaance on 1/30/2016.
 */


import java.util.Date;
import java.io.Serializable;

public class ComputerInfo implements Serializable {
    public static void main (String args[]) {
        // Instantiate a date object
        Date date = new Date();
        // display time and date using toString()
        String time = String.format("Date, Time : %tc",date);
        // Show time
        System.out.println(time);
        // Java class path
        System.out.println( "Java"+ " Class Path:"+ " " + System.getProperty("java.class.path"));
        // JRE Vendor name
        System.out.println( "Java"+ " Vendor:"+ " " + System.getProperty("java.vendor"));
        // JRE Version number (JDK)
        System.out.println( "Java"+ " Version:"+ " " + System.getProperty("java.version"));
        // OS architecture
        System.out.println( "OS"+ " Architecture:"+ " " + System.getProperty("os.arch"));
        // OS name
        System.out.println( "OS"+ " Name:"+ " " + System.getProperty("os.name"));
        // OS version
        System.out.println( "OS"+ " Version:"+ " " + System.getProperty("os.version"));
        // User home directory
        System.out.println( "User"+ " Home"+" Directory:" + " " + System.getProperty("user.home"));
        // User account name
        System.out.println( "User"+ " Name:"+ " " + System.getProperty("user.name"));


    }
}
