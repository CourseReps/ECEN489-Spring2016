import java.sql.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by tbranyon on 1/29/16.
 */
public class RssiDbWriter
{
    Connection c = null;
    Statement stmt = null;
    try{
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:data.db");
        c.setAutoCommit(false);
        System.out.println("Database opened");

        stmt = c.createStatement();
        String cmd = "CREATE TABLE IF NOT EXISTS wlan0(INT rssi, TEXT MAC Address UNIQUE, INT timestamp)";
        stmt.executeUpdate(cmd);
    }catch(Exception e){
        System.err.println("An error occurred");
        System.err.println(e);
        System.exit(1);
    }
    System.out.println("Table created");

    try{
        String cmd = "INSERT OR REPLACE INTO TBL1 VALUES (01,'TEST',10.0," + System.currentTimeMillis() + ");";
        stmt.executeUpdate(cmd);
        stmt.close();
        c.commit();
        c.close();
    }catch(Exception e){
        System.err.println("An error occurred");
        System.err.println(e);
        System.exit(2);
    }
}
