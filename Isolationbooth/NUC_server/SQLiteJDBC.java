import java.sql.*;
import java.util.*;
import static java.lang.Math.*;
public class SQLiteJDBC
{
    public static void main(String[] args) {
        while (true) {
            Map<Double,String> avg = new HashMap<>(); //add data to map
            double rssimax = 0;  //overall max rssi
            String mac = ""; //mac address of max rssi
            Connection c = null;
            Statement stmt = null;
            //opening database
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:data.db");
                c.setAutoCommit(false);
                System.out.println("Database opened");
                stmt = c.createStatement();

            } catch (Exception e) {
                System.err.println("An error occurred");
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(1);
            }
            //getting number of entries in table for iteration
            try {
                String numentry = "SELECT COUNT(*) FROM wlan0";
                ResultSet numretwlan0 = stmt.executeQuery(numentry);
                // get the number of rows from the result set
                numretwlan0.next();
                int rowCount = numret.getInt(1);
                System.out.println(rowCount);
                numretwlan0.close();
                //stmt.close();

                //iterating through tables to find max rssi
                for (int id = 0; id < rowCount; id++) {
                    String query = "SELECT MAC_Address FROM wlan0 WHERE ID = " + id;
                    ResultSet macrs = stmt.executeQuery(query);
                    String macst = "\"" + macrs.getString("MAC_Address") + "\"";
                    macrs.close();
                    ////stmt.close();

                    query = "SELECT * FROM wlan0 WHERE MAC_Address =" + macst;
                    ResultSet rssi0 = stmt.executeQuery(query);
                    int rssiwlan0 = rssi0.getInt("rssi");
                    ////stmt.close();
                    rssi0.close();

                    query = "SELECT * FROM wlan1 WHERE MAC_Address =" + macst;
                    ResultSet rssi1 = stmt.executeQuery(query);
                    int rssiwlan1 = rssi1.getInt("rssi");
                    //stmt.close();
                    rssi1.close();

                    query = "SELECT * FROM wlan2 WHERE MAC_Address =" + macst;
                    ResultSet rssi2 = stmt.executeQuery(query);
                    int rssiwlan2 = rssi2.getInt("rssi");
                    //stmt.close();
                    rssi2.close();

                    double rssimag = sqrt((rssiwlan0 * rssiwlan0) + (rssiwlan1 * rssiwlan1) + (rssiwlan2 * rssiwlan2));

                    avg.put(rssimag,macst);
                }

            } catch (Exception e) {
                System.err.println("An error occurred");
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(2);
            }
      
            for (Map.Entry<Double, String> entry : avg.entrySet()) {
                double rssiVal = entry.getKey();
                if(rssiVal > rssimax){
                    rssimax=rssiVal;
                }
            }
            mac = avg.get(rssimax);
           if(rssimax < 40){
            	try{
            	System.out.println("Executing picture request");
                Runtime.getRuntime().exec("java client");
                }catch(Exception e){System.err.println(e); System.exit(3);}
            }
            
            System.out.println(mac);
            avg.clear();
            try{
                long unixTime = System.currentTimeMillis() / 1000L;

                String numentry = "SELECT COUNT(*) FROM wlan0";
                ResultSet numret0 = stmt.executeQuery(numentry);
                // get the number of rows from the result set
                numret0.next();
                int wlan0cnt = numret0.getInt(1);
                //stmt.close();
                numret0.close();

                numentry = "SELECT COUNT(*) FROM wlan1";
                ResultSet numret1 = stmt.executeQuery(numentry);
                // get the number of rows from the result set
                numret1.next();
                int wlan1cnt = numret1.getInt(1);
                //stmt.close();
                numret1.close();

                numentry = "SELECT COUNT(*) FROM wlan2";
                ResultSet numret2 = stmt.executeQuery(numentry);
                // get the number of rows from the result set
                numret2.next();
                int wlan2cnt = numret2.getInt(1);
                //stmt.close();
                numret2.close();

                for (int id = 0; id < wlan0cnt; id++) {
                    String query = "SELECT TIME FROM wlan0 WHERE ID =" + id;
                    ResultSet time0 = stmt.executeQuery(query);
                    int timestamp = time0.getInt(1);
                    //stmt.close();
                    time0.close();
                    if(unixTime-timestamp > 120){
                        String delete = "DELETE FROM wlan0 WHERE Timestamp =" + timestamp;
                        stmt.executeQuery(delete);
                        //stmt.close();
                    }
                }
                for (int id = 0; id < wlan1cnt; id++) {
                    String query = "SELECT TIME FROM wlan1 WHERE ID =" + id;
                    ResultSet time1 = stmt.executeQuery(query);
                    int timestamp = time1.getInt(1);
                    //stmt.close();
                    time1.close();
                    if(unixTime-timestamp > 120){
                        String delete = "DELETE FROM wlan1 WHERE Timestamp =" + timestamp;
                        stmt.executeQuery(delete);
                        //stmt.close();
                    }
                }
                for (int id = 0; id < wlan2cnt; id++) {
                    String query = "SELECT TIME FROM wlan2 WHERE ID =" + id;
                    ResultSet time2 = stmt.executeQuery(query);
                    int timestamp = time2.getInt(1);
                    //stmt.close();
                    time2.close();
                    if(unixTime-timestamp > 120){
                        String delete = "DELETE FROM wlan2 WHERE Timestamp =" + timestamp;
                        stmt.executeQuery(delete);
                        //stmt.close();
                    }
                }

            }
            catch (Exception e){
                System.err.println("An error occurred");
                System.exit(3);
            }
            try {
	            stmt.close();
                Thread.sleep(10000);
            }
            catch(Exception e) {
                System.err.println("An error occurred");
                System.exit(4);
            }
        }
    }
}
