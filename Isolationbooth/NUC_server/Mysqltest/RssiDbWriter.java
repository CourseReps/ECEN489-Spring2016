import java.sql.*;
//import java.util.concurrent.TimeUnit;

/**
 * Created by tbranyon on 1/29/16.
 */
public class RssiDbWriter
{
    Connection c = null;
    Statement stmt = null;
    public static int id0 = 0;
    public static int id1 = 0;
    public static int id2 = 0;
    
    public RssiDbWriter()
    {   
        try{
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/data","root","isobooth");
            c.setAutoCommit(false);
            System.out.println("Database opened");

            stmt = c.createStatement();
            String cmd = "CREATE TABLE IF NOT EXISTS wlan0(rssi INT, MAC_Address VARCHAR(17) PRIMARY KEY, Timestamp INT)";
            stmt.executeUpdate(cmd);
            cmd = "CREATE TABLE IF NOT EXISTS wlan1(rssi INT, MAC_Address VARCHAR(17) PRIMARY KEY, Timestamp INT)";
            stmt.executeUpdate(cmd);
            cmd = "CREATE TABLE IF NOT EXISTS wlan2(rssi INT, MAC_Address VARCHAR(17) PRIMARY KEY, Timestamp INT)";
            stmt.executeUpdate(cmd);
        }catch(Exception e){
            System.err.println("An error occurred");
            System.err.println(e);
            System.exit(1);
        }
        System.out.println("Table created");
    }

    public void writeDB(int rssi, String macAddr, long unixTime, int tableNum)
    {  
        try{
            String cmd = "REPLACE INTO wlan" + tableNum + " VALUES (" + rssi + ",\"" + macAddr + "\"," + unixTime + ");";
            stmt.executeUpdate(cmd);
            c.commit();
        }catch(Exception e){
            System.err.println("An error occurred");
            System.err.println(e);
            System.exit(2);
        }
    }

    public void close()
    {
        try{
            stmt.close();
            c.close();
        }catch(Exception e){System.err.println(e);}
    }
}
