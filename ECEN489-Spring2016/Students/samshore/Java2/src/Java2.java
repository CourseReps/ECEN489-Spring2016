import java.util.Date;
/**
 * Created by Sam on 1/31/2016.
 */
public class Java2 {
        public static void main(String[] args){
            // print date and time
            Date date = new Date();
            System.out.println(date.toString());

            // print java class path
            System.out.print("java class path: ");
            System.out.println(System.getProperty("java.class.path"));

            // print jre vendor name
            System.out.print("jre vendor name: ");
            System.out.println(System.getProperty("java.vendor"));

            // print jre version number
            System.out.print("jre version number: ");
            System.out.println(System.getProperty("java.version"));

            // print os architecture
            System.out.print("operating system architecture: ");
            System.out.println(System.getProperty("os.arch"));

            // print os name
            System.out.print("operating system: ");
            System.out.println(System.getProperty("os.name"));

            // print os version
            System.out.print("os version: ");
            System.out.println(System.getProperty("os.version"));

            // print user home directory
            System.out.print("user home directory: ");
            System.out.println(System.getProperty("user.home"));

            // print user name
            System.out.print("user name: ");
            System.out.println(System.getProperty("user.name"));
    }

}
