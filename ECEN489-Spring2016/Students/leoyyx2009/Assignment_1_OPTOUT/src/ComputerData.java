/**
 * Created by YYX on 1/25/16.
 * ECEN689-610:SPTP: DATA ACQ EMBEDDED SYS
 * Auther:  Yanxiang Yang
 * Assignment1: Opt-Out Challenge 1
 */

// -----------------------------------------------------------------
// imports
import java.util.*;
import java.text.SimpleDateFormat;

// -----------------------------------------------------------------
// Define ComputerDate class that contains three instance variable
// and methods to set and get their value
// -----------------------------------------------------------------
public class ComputerData{

    // variable time is to store the information of current time
    // osVersion is to store the information of operating system
    // javaVersion is to store the information of the version of JAVA
    private String time, osVersion, javaVersion;

    // constructor to initialize the variables
    public ComputerData(){
        time =( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
        osVersion = System.getProperty("os.name");
        javaVersion = System.getProperty("java.version");
    }

    // method to retrieve time
    public String getTime(){
        return time;
    }

    // method to retrieve javaVersion
    public String getJavaVersion(){
        return javaVersion;
    }

    // method to retrieve osVersion
    public String getOsVersion(){
        return osVersion;
    }
}
