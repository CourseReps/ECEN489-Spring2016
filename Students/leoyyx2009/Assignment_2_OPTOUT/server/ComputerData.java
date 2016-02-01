/**
 * Created by YYX on 1/30/16.
 * ECEN689-610:SPTP: DATA ACQ EMBEDDED SYS
 * Auther:  Yanxiang Yang
 * Assignment2: OPT-OUT
 */
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class ComputerData implements Serializable
{
    private String classpath;
    private String jreVendor;
    private String jreVersion;
    private String OS_arch;
    private String OS_name;
    private String OS_version;
    private String userHome;
    private String userName;
    private String dateTime;

    public ComputerData()
    {
        classpath = System.getProperty("java.class.path");
        jreVendor = System.getProperty("java.vendor");
        jreVersion = System.getProperty("java.version");
        OS_arch = System.getProperty("os.arch");
        OS_name = System.getProperty("os.name");
        OS_version = System.getProperty("os.version");
        userHome = System.getProperty("user.home");
        userName = System.getProperty("user.name");

        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curTimeDate = new Date();
        dateTime = fmt.format(curTimeDate);
    }

    public String toString()
    {
        return    ("Timestamp     : " + dateTime + "\n" +
                "Classpath     : " + classpath + "\n" +
                "JRE Vendor    : " + jreVendor + "\n" +
                "JRE Version   : " + jreVersion + "\n" +
                "OS Arch       : " + OS_arch + "\n" +
                "OS Name       : " + OS_name + "\n" +
                "OS Version    : " + OS_version + "\n" +
                "User Home Dir : " + userHome + "\n" +
                "Username      : " + userName
        );
    }
}