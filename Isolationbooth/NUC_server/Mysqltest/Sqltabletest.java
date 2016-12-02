import java.sql.*;
import java.util.Date;
//import java.util.concurrent.TimeUnit;

/**
 * Created by tbranyon on 1/29/16.
 */
 public class Sqltabletest{       
    public static void main(String[] args)
    {   
        try{
		Connection c = null;
   		Statement stmt = null;
   	 	int id0 = 0;
   		int id1 = 0;
    		int id2 = 0;
		int tableNum=0;
		int rssi=30;
		String macAddr="ba:e8:56:e1:9f:00";
		java.util.Date d = new java.util.Date();
		long unixTime =  d.getTime()/1000;

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
/*
            String cmd = "REPLACE INTO wlan" + tableNum + " VALUES (" + rssi + ",\"" + macAddr + "\"," + unixTime + ");";
            stmt.executeUpdate(cmd);*/
            c.commit();
        }catch(Exception e){
            System.err.println("An error occurred");
            System.err.println(e);
            System.exit(2);
        }
        System.out.println("Table created");
   }
}
