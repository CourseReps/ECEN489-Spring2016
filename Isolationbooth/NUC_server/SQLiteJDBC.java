import java.sql.*;
import java.util.*;
import static java.lang.Math.*;
public class SQLiteJDBC
{
    public static void main(String[] args) 
    {
        while (true) 
        {
            Map<Double,String> avg = new HashMap<>(); //add data to map
            double rssimax = 0;  //overall max rssi
            String mac = ""; //mac address of max rssi
            Connection c = null;
            Statement stmt = null;
            ResultSet numretwlan0 = null;
            ResultSet numretwlan1 = null;
            ResultSet numretwlan2 = null;
            int rowCount = 0;
            int rowCount1 = 0;
            int rowCount2 = 0;
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

            try{
                String numentry = "SELECT COUNT(*) FROM wlan0";
                numretwlan0 = stmt.executeQuery(numentry);
                // get the number of rows from the result set
                numretwlan0.next();
                rowCount = numretwlan0.getInt(1);
                System.out.println(rowCount);
                numretwlan0.close();

                numentry = "SELECT COUNT(*) FROM wlan1";
                numretwlan1 = stmt.executeQuery(numentry);
                // get the number of rows from the result set
                numretwlan1.next();
                rowCount1 = numretwlan1.getInt(1);
                System.out.println(rowCount1);
                numretwlan1.close();

                numentry = "SELECT COUNT(*) FROM wlan2";
                numretwlan2 = stmt.executeQuery(numentry);
                // get the number of rows from the result set
                numretwlan2.next();
                rowCount2 = numretwlan2.getInt(1);
                System.out.println(rowCount2);
                numretwlan2.close();
            }catch(Exception e){
                System.err.println(e);
            }
                
            //getting number of entries in table for iteration
            try{
                String query = "SELECT Mac_Address FROM wlan0";
                ResultSet macrs = stmt.executeQuery(query);

                //iterating through tables to find max rssi
                for (int id = 0; id < rowCount; id++) 
                {
                    int rssiwlan0 = 0;
                    int rssiwlan1 = 0;
                    int rssiwlan2 = 0;
                    macrs.next();

                    String macst = macrs.getString(1);

                    Statement s1 = c.createStatement();
                    query = "SELECT * FROM wlan0"; 
                    ResultSet rssi0 = s1.executeQuery(query);
                    for (int id1 = 0; id1 < rowCount; id1++)
                    {
                        rssi0.next();
                        String macwlan0 = rssi0.getString(2);
                        if(macwlan0.equals(macst))
                            rssiwlan0 = rssi0.getInt(1);
                    }
                    rssi0.close();

                    query = "SELECT * FROM wlan1";
                    ResultSet rssi1 = s1.executeQuery(query);
                    for (int id2 = 0; id2 < rowCount1; id2++)
                    {
                        rssi1.next();
                        String macwlan1 = rssi1.getString(2);
                        if(macwlan1.equals(macst))
                            rssiwlan1 = rssi1.getInt(1);
                    }
                    rssi1.close();

                    query = "SELECT * FROM wlan2";
                    ResultSet rssi2 = s1.executeQuery(query);
                    for (int id3 = 0; id3 < rowCount2; id3++)
                    {
                        rssi2.next();
                        String macwlan2 = rssi2.getString(2);
                        if(macwlan2.equals(macst))
                            rssiwlan2 = rssi2.getInt(1);
                    }
                    rssi2.close();
                    s1.close();

                    double rssimag = sqrt((rssiwlan0 * rssiwlan0) + (rssiwlan1 * rssiwlan1) + (rssiwlan2 * rssiwlan2));
                    avg.put(rssimag,macst);

                    System.out.println(macst);
                }
                macrs.close();
            }catch (Exception e) {
                System.err.println(e);
                System.exit(2);
            }
      
            for (Map.Entry<Double, String> entry : avg.entrySet()) 
            {
                double rssiVal = entry.getKey();
                if(rssiVal > rssimax)
                    rssimax=rssiVal;
            }
            mac = avg.get(rssimax);
            if(rssimax < 40)
            {
            	try{
            	System.out.println("Executing picture request");
                Runtime.getRuntime().exec("java client");
                }catch(Exception e){
                    System.err.println(e);
                    System.exit(3);
                }
            }
            
            System.out.println(mac);
            avg.clear();
            try{
                long unixTime = System.currentTimeMillis() / 1000L;

                String query = "SELECT Timestamp FROM wlan0";
                ResultSet time0 = stmt.executeQuery(query);
                
                for (int id = 0; id < rowCount; id++) 
                {
                    time0.next();
                    int timestamp = time0.getInt(1);

                    if(unixTime-timestamp > 120)
                    {
                        String delete = "DELETE FROM wlan0 WHERE Timestamp =" + timestamp;
                        stmt.executeQuery(delete);
                    }
                    
                }
                time0.close();

                query = "SELECT Timestamp FROM wlan1";
                ResultSet time1 = stmt.executeQuery(query);
                for (int id = 0; id < rowCount1; id++) 
                {
                    time1.next();
                    int timestamp = time1.getInt(1);
                    
                    if(unixTime-timestamp > 120)
                    {
                        String delete = "DELETE FROM wlan1 WHERE Timestamp =" + timestamp;
                        stmt.executeQuery(delete);
                    }
                }
                time1.close();

                query = "SELECT Timestamp FROM wlan2";
                ResultSet time2 = stmt.executeQuery(query);
                for (int id = 0; id < rowCount2; id++) 
                {
                    time2.next();
                    int timestamp = time2.getInt(1);

                    if(unixTime-timestamp > 120)
                    {
                        String delete = "DELETE FROM wlan2 WHERE Timestamp =" + timestamp;
                        stmt.executeQuery(delete);
                    }
                }
                time2.close();
            }catch (Exception e){
                System.err.println(e);
                System.exit(4);
            }

            try{
	            stmt.close();
                Thread.sleep(10000);
            }catch(Exception e) {
                System.err.println(e);
                System.exit(5);
            }
        }
    }
}