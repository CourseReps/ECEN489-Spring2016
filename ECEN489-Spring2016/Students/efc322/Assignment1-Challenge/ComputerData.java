import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fanchao Zhou on 1/25/2016.
 */
public class ComputerData {
    private String time;
    private String javaVer;
    private String osVer;

    public ComputerData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/D/Y E H:m:s z");
        time = dateFormat.format(new Date());
        javaVer = System.getProperty("java.version");
        osVer = System.getProperty("os.name") + " " + System.getProperty("os.version");
    }
    public String getTime(){
        return time;
    }
    public String getJavaVer(){
        return javaVer;
    }
    public String getOSVer(){
        return osVer;
    }
}
