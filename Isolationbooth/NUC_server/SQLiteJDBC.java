import java.sql.*;
import java.util.*;
import static java.lang.Math.*;
public class SQLiteJDBC
{
    public static void main(String[] args) 
    {
        //opening connection to database
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
        //continue looping in order to process new database information
        while (true) 
        {

            Map<Double,String> avg = new HashMap<>(); //add data to map
            double rssimax = 0;  //overall max rssi
            String mac = ""; //mac address of max rssi
            
            Statement stmt = null;
            ResultSet numretwlan0 = null; //resultset for rows in table wlan0
            ResultSet numretwlan1 = null; //resultset for rows in table wlan1
            ResultSet numretwlan2 = null; //resultset for rows in table wlan2
            int rowCount = 0;
            int rowCount1 = 0;
            int rowCount2 = 0;
            
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
                continue;
            }
                
            //getting number of entries in table for iteration
           try{
                long unixTime = System.currentTimeMillis() / 1000L; //current time for comparison
                    int rssiwlan0 = 0;
                    int rssiwlan1 = 0;
                    int rssiwlan2 = 0;
                    
                    Statement s1 = c.createStatement();
                    
                    String query = "SELECT * FROM wlan0"; 
                    ResultSet rssi0 = s1.executeQuery(query);
                    //iterate through result set to find old entries to delete
                    while(rssi0.next()){
                        int timestamp = rssi0.getInt(3);

                        if(unixTime-timestamp > 180){
                            String delete = "DELETE FROM wlan0 WHERE Timestamp = ?";
                            PreparedStatement time = c.prepareStatement(delete);
                            time.setInt(1,timestamp);
                            time.execute();
                            time.close();
                        }
                    }
                    rssi0.close();
                    

                    query = "SELECT * FROM wlan1";
                    ResultSet rssi1 = s1.executeQuery(query);
                    //iterate through result set to find old entries to delete
                    while(rssi1.next())
                    {
                        int timestamp = rssi1.getInt(3);

                        if(unixTime-timestamp > 180){
                            String delete = "DELETE FROM wlan1 WHERE Timestamp = ?";
                            PreparedStatement time = c.prepareStatement(delete);
                            time.setInt(1,timestamp);
                            time.execute();
                            time.close();
                        }
                    }
                    rssi1.close();

                    query = "SELECT * FROM wlan2";
                    ResultSet rssi2 = s1.executeQuery(query);
                    //iterate through result set to find old entries to delete
                    while(rssi2.next()){
                        int timestamp = rssi2.getInt(3);

                        if(unixTime-timestamp > 180){
                            String delete = "DELETE FROM wlan2 WHERE Timestamp = ?";
                            PreparedStatement time = c.prepareStatement(delete);
                            time.setInt(1,timestamp);
                            time.execute();
                            time.close();
                        }
                    }
                    rssi2.close();
                    s1.close();
                    
            }catch(Exception e){
                    System.err.println(e);
                    System.exit(2);
                }

            try{
                String query = "SELECT Mac_Address FROM wlan0";
                ResultSet macrs = stmt.executeQuery(query);

                //iterating through tables to find max rssi
                while (macrs.next()) 
                {
                    int rssiwlan0 = 0;
                    int rssiwlan1 = 0;
                    int rssiwlan2 = 0;

                    String macst = macrs.getString(1); //iterates through each mac_address
                    Statement s1 = c.createStatement();
                    query = "SELECT * FROM wlan0"; 
                    ResultSet rssi0 = s1.executeQuery(query);

                    while(rssi0.next())
                    {
                            String macwlan0 = rssi0.getString(2);

                            if(macwlan0.equals(macst))  //compares with mac address in first loop if the same get rssi
                                rssiwlan0 = rssi0.getInt(1);
                    }
                    rssi0.close();

                    query = "SELECT * FROM wlan1";
                    ResultSet rssi1 = s1.executeQuery(query);

                    while(rssi1.next())
                    {
                        String macwlan1 = rssi1.getString(2);

                            if(macwlan1.equals(macst)) //compares with mac address in first loop if the same get rssi
                                rssiwlan1 = rssi1.getInt(1);
                    }
                    rssi1.close();

                    query = "SELECT * FROM wlan2";
                    ResultSet rssi2 = s1.executeQuery(query);

                    while(rssi2.next())
                    {
                        String macwlan2 = rssi2.getString(2);
                            if(macwlan2.equals(macst)) //compares with mac address in first loop if the same get rssi
                                rssiwlan2 = rssi2.getInt(1);
                    }
                    rssi2.close();
                    s1.close();
                    
					if(rssiwlan0 != 0 && rssiwlan1 != 0 && rssiwlan2 !=0){
                    	double rssimag = sqrt((rssiwlan0 * rssiwlan0) + (rssiwlan1 * rssiwlan1) + (rssiwlan2 * rssiwlan2)); //magnitude of rssi
                    	avg.put(rssimag,macst); //adds rssi and associated MAC address to map
                    }

                }
                macrs.close();
            }catch (Exception e) {
                System.err.println(e);
                System.exit(2);
            }
            //iterates through map to find maximum rssi and associated MAC address
            for (Map.Entry<Double, String> entry : avg.entrySet()){
                double rssiVal = entry.getKey();

                if(rssiVal > rssimax && rssiVal!=0)
                    rssimax=rssiVal;
            }
            mac = avg.get(rssimax);
			System.out.println(mac);
			System.out.println(rssimax);
            if(rssimax < 60 && rssimax != 0) //threshold for device to be considered within the area
            {
            	try{
            	System.out.println("Executing picture request");  //runs get picture from android camera program
                System.out.println(mac);
                System.out.println(rssimax);
				String cmd = "java client " + mac;
                Runtime.getRuntime().exec(cmd);
                String delete = "DELETE FROM wlan1 WHERE Mac_Address = ?";
                PreparedStatement time = c.prepareStatement(delete);
                time.setString(1,mac);
                time.execute();
                time.close();
                Thread.sleep(5000);
				System.out.println("pic received");
                }catch(Exception e){
                    System.err.println(e);
                    System.exit(3);
                }
            }
            avg.clear(); //clears map for next loop

            try{
	            stmt.close();
                Thread.sleep(2000);
            }catch(Exception e) {
                System.err.println(e);
                System.exit(5);
            }
        }
    }
}
