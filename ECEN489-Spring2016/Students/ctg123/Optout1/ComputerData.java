/**
 * Created by Chaance on 1/26/2016.
 */

import java.util.Date;

public class ComputerData {

    public static void main (String[] args)
    {
        //  Instantiate a date object
        Date date = new Date();

        // display time and date using toString()
        String time = String.format("Date, Time : %tc",date);
        // Show time
        System.out.println(time);

        //--------- OS Properties within class---------//
        System.out.println( "OS" + " Name:"+ " " + System.getProperty("os.name"));
        System.out.println( "OS" +  " Time:"+ " " + System.getProperty("os.version"));

        // JRE Version number (JDK)
        System.out.println( "Java"+ " Version:"+ " " + System.getProperty("java.version"));
    }
}
