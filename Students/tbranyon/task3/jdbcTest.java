import java.sql.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by tbranyon on 1/29/16.
 */
public class jdbcTest
{
    public static void main(String[] args)
    {
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("Database opened");

            stmt = c.createStatement();
            String cmd = "CREATE TABLE IF NOT EXISTS TBL1(ID INT PRIMARY KEY, Info TEXT, Value REAL, Timestamp INT)";
            stmt.executeUpdate(cmd);
        }catch(Exception e){
            System.err.println("An error occurred");
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(1);
        }
        System.out.println("Table created");

        try{
            String cmd = "INSERT OR REPLACE INTO TBL1 VALUES (01,'TEST',10.0," + System.currentTimeMillis() + ");";
            stmt.executeUpdate(cmd);
            TimeUnit.SECONDS.sleep(1);
            cmd = "INSERT OR REPLACE INTO TBL1 VALUES (02,'test',20.0," + System.currentTimeMillis() + ");";
            stmt.executeUpdate(cmd);
            stmt.close();
            c.commit();
            c.close();
        }catch(Exception e){
            System.err.println("An error occurred");
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(2);
        }
        System.out.println("Table updated");
    }
}
