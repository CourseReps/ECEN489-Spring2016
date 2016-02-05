import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tbranyon on 1/23/16.
 */
public class ComputerData
{
    private String jreVer, time, OS;

    public ComputerData()
    {
        jreVer = System.getProperty("java.version");
        OS = System.getProperty("os.name") + " " + System.getProperty("os.version");
        time = "";
    }

    public String getJreVer()
    {
        return jreVer;
    }

    public String getOS()
    {
        return OS;
    }

    public String getTime()
    {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curTimeDate = new Date();
        time = fmt.format(curTimeDate);
        return time;
    }

    public String toString()
    {
        this.getTime();
        String retval = "OS: " + OS + "\n";
        retval += "Java Version: " + jreVer + "\n";
        retval += "Current Local Time: " + time;
        return retval;
    }
}
