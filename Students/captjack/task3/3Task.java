import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class HelloDatabase
{
    public static void main (String[] args) throws Exception
    {
        // register the driver 
        String sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);

        // now we set up a set of fairly basic string variables to use in the body of the code proper
        String sTempDb = "hello.db";
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + sTempDb;
        // which will produce a legitimate Url for SqlLite JDBC :
        // jdbc:sqlite:hello.db
        int iTimeout = 30;
        String sMakeTable = "CREATE TABLE IF NOT EXISTS dummy (id numeric, response text)";
        String sMakeInsert = "INSERT INTO dummy VALUES(1,'Hello from the database')";
        String sMakeInsert2 = "INSERT INTO dummy VALUES(2,'This is Task 3')";
        String deleter = "DELETE FROM dummy";
        String sMakeSelect = "SELECT response from dummy";

        // create a database connection
        Connection conn = DriverManager.getConnection(sDbUrl);
        try {
            Statement stmt = conn.createStatement();
            try {
                stmt.setQueryTimeout(iTimeout);
                stmt.executeUpdate( sMakeTable );
                stmt.executeUpdate(sMakeInsert);
                stmt.executeUpdate(sMakeInsert2);
                stmt.executeUpdate(deleter);
                ResultSet rs = stmt.executeQuery(sMakeSelect);
                try {
                    while(rs.next())
                    {
                        String sResult = rs.getString("response");
                        System.out.println(sResult);
                    }
                } finally {
                    try { rs.close(); } catch (Exception ignore) {}
                }
            } finally {
                try { stmt.close(); } catch (Exception ignore) {}
            }
        } finally {
            try { conn.close(); } catch (Exception ignore) {}
        }
    }

}