import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ComputerData {
private
	String javaVersion, currTime, osVersion;	
	

 ComputerData(){
	javaVersion = System.getProperty("java.version");
	currTime = ( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
	osVersion = System.getProperty("os.name");
}
	public String getJavaVersion()
	{
		return javaVersion;
	}
	public String getCurrTime()
	{
		return currTime;
	}
	
	public String getOSVersion()
	{
		return  osVersion;
	}
	
} 	
