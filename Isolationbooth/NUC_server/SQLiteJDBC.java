import java.sql.*;
import java.util.*;
import static java.lang.Math.*;
public class SQLiteJDBC
{
    public static void main(String[] args) 
    {
        Connection c = null;
        try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:data.db");
                c.setAutoCommit(true);
                System.out.println("Database opened");
                
            } catch (Exception e) {
                System.err.println("An error occurred");
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(1);
            }
        while (true) 
        {

            Map<Double,String> avg = new HashMap<>(); //add data to map
            double rssimax = 0;  //overall max rssi
            String mac = ""; //mac address of max rssi
            
            Statement stmt = null;
            ResultSet numretwlan0 = null;
            ResultSet numretwlan1 = null;
            ResultSet numretwlan2 = null;
            int rowCount = 0;
            int rowCount1 = 0;
            int rowCount2 = 0;
            //opening database
            

            try{
                stmt = c.createStatement();

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
           /* try{
                long unixTime = System.currentTimeMillis() / 1000L;
                //iterating through tables to find max rssi
                    int rssiwlan0 = 0;
                    int rssiwlan1 = 0;
                    int rssiwlan2 = 0;
                    
                    Statement s1 = c.createStatement();
                    
                    String query = "SELECT * FROM wlan0"; 
                    ResultSet rssi0 = s1.executeQuery(query);
                        
                    while(rssi0.next()){
                        int timestamp = rssi0.getInt(3);
                        System.out.println(timestamp);
                        if(unixTime-timestamp > 120){
                            String delete = "DELETE FROM wlan0 WHERE Timestamp = ?";
                            PreparedStatement time = c.prepareStatement(delete);
                            time.setInt(1,timestamp);
                            time.execute();
                            time.close();
                            System.out.println(delete);
                        }
                    }
                    rssi0.close();
                    

                    query = "SELECT * FROM wlan1";
                    ResultSet rssi1 = s1.executeQuery(query);
                    while(rssi1.next())
                    {
                        int timestamp = rssi1.getInt(3);

                        if(unixTime-timestamp > 120){
                            String delete = "DELETE FROM wlan1 WHERE Timestamp = ?";
                            PreparedStatement time = c.prepareStatement(delete);
                            time.setInt(1,timestamp);
                            time.execute();
                            time.close();
                            System.out.println(delete);
                        }
                    }
                    rssi1.close();

                    query = "SELECT * FROM wlan2";
                    ResultSet rssi2 = s1.executeQuery(query);
                    while(rssi2.next()){
                        int timestamp = rssi2.getInt(3);

                        if(unixTime-timestamp > 120){
                            String delete = "DELETE FROM wlan2 WHERE Timestamp = ?";
                            PreparedStatement time = c.prepareStatement(delete);
                            time.setInt(1,timestamp);
                            time.execute();
                            time.close();
                            System.out.println(delete);
                        }
                    }
                    rssi2.close();
                    s1.close();
                    
            }catch(Exception e){
                    System.err.println(e);
                    System.exit(2);
                }*/

        try{
                String query = "SELECT Mac_Address FROM wlan0";
                ResultSet macrs = stmt.executeQuery(query);
                long unixTime = System.currentTimeMillis() / 1000L;

                //iterating through tables to find max rssi
                while (macrs.next()) 
                {
                    int rssiwlan0 = 0;
                    int rssiwlan1 = 0;
                    int rssiwlan2 = 0;

                    String macst = macrs.getString(1);
                    Statement s1 = c.createStatement();
                    query = "SELECT * FROM wlan0"; 
                    ResultSet rssi0 = s1.executeQuery(query);

                    while(rssi0.next())
                    {
                            String macwlan0 = rssi0.getString(2);
                            System.out.println(macwlan0);
                            if(macwlan0.equals(macst))
                                rssiwlan0 = rssi0.getInt(1);
                    }
                    rssi0.close();

                    query = "SELECT * FROM wlan1";
                    ResultSet rssi1 = s1.executeQuery(query);

                    while(rssi1.next())
                    {
                        String macwlan1 = rssi1.getString(2);
                        System.out.println(macwlan1);
                            if(macwlan1.equals(macst))
                                rssiwlan1 = rssi1.getInt(1);
                    }
                    rssi1.close();

                    query = "SELECT * FROM wlan2";
                    ResultSet rssi2 = s1.executeQuery(query);

                    while(rssi2.next())
                    {
                        String macwlan2 = rssi2.getString(2);
                        System.out.println(macwlan2);
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
            System.out.println("pic finished");
            /*System.out.println(mac);
            avg.clear();
*/
            try{
	            stmt.close();
                //c.close();
                Thread.sleep(1000);
            }catch(Exception e) {
                System.err.println(e);
                System.exit(5);
            }
        }
    }
}