/**
 * Created by Kyle on 1/26/2016.
 */
public class ComputerData {
    String version = "", operatingSystem = "";
    long time;

    public ComputerData() {
    }

    public void setVersion() {
        version = Runtime.class.getPackage().getImplementationVersion();
    }

    public void setTime() {
        time = System.currentTimeMillis();
    }

    public void setOperatingSystem() {
        operatingSystem = System.getProperty("os.name");
    }

    public void printData() {
        System.out.printf("time: %s\nJava verion: %s \nOperating System: %s ", time, version, operatingSystem);
    }


    public static void main(String[] args) {

        ComputerData compData = new ComputerData();
        compData.setVersion();
        compData.setOperatingSystem();
        compData.setTime();
        compData.printData();
    }
}
