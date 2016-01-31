import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fanchao Zhou on 1/31/2016.
 */

public class ComputerData implements Serializable {
    private String classPath;
    private String javaVender;
    private String javaVer;
    private String osArch;
    private String osName;
    private String osVer;
    private String userHome;
    private String userName;
    private String time;

    public ComputerData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = dateFormat.format(new Date());
        javaVer = System.getProperty("java.version");
        javaVender = System.getProperty("java.vendor");
        osVer = System.getProperty("os.version");
        osName = System.getProperty("os.name");
        classPath = System.getProperty("java.class.path");
        osArch = System.getProperty("os.arch");
        userName = System.getProperty("user.name");
        userHome = System.getProperty("user.home");
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
    public String getOSName(){
        return osName;
    }
    public String getJavaVender(){
        return javaVender;
    }
    public String getClassPath(){
        return classPath;
    }
    public String getUserName(){
        return userName;
    }
    public String getUserHome(){
        return userHome;
    }
    public String getOSArch(){
        return osArch;
    }
}
