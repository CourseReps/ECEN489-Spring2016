/**
 * Created by Fanchao Zhou on 1/25/2016.
 */
public class PrintData {
    public static void main(String[] args){
        ComputerData computerData = new ComputerData();

        System.out.printf("System Time: %s%n", computerData.getTime());
        System.out.printf("Java Version: %s%n", computerData.getJavaVer());
        System.out.printf("OS Version: %s%n", computerData.getOSVer());
    }
}
