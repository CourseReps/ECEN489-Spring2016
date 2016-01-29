import java.io.IOException;
import java.io.Serializable;

/**
 * Created by tbranyon on 1/29/16.
 */
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
    }

    public String toString()
    {
        return    ("Classpath     : " + classpath + "\n" +
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
