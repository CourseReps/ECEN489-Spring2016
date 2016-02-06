/**
 * Created by YYX on 1/25/16.
 * ECEN689-610:SPTP: DATA ACQ EMBEDDED SYS
 * Auther:  Yanxiang Yang
 * Assignment1: Opt-Out Challenge 1
 */

// -----------------------------------------------------------------
// Define ComputerDatePrint class that creates and manipulates a ComputerData object
// -----------------------------------------------------------------
public class ComputerDataPrint {

    public static void main (String[] args)
    {
        // create a ComputerData object and assign it to MyInstance
        ComputerData MyInstance = new ComputerData();

        // display the information in ComputerData
        System.out.println("Current Time is: "+MyInstance.getTime());
        System.out.println("Java version is: "+MyInstance.getJavaVersion());
        System.out.println("OS is: "+MyInstance.getOsVersion());
    }
}
