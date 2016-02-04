/**
 * Created by Fanchao Zhou on 1/29/2016.
 */
import java.sql.*;

public class jdbc
{
    public static void main( String args[] )
    {
        Connection test1Connection = null;
        Statement test1Stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            test1Connection = DriverManager.getConnection("jdbc:sqlite:test1.db");

            System.out.println("Opened database successfully");

            test1Stmt = test1Connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS A_and_M " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " AGE            INT     NOT NULL, " +
                    " DEPARTMENT     TEXT    NOT NULL)";
            test1Stmt.executeUpdate(sql);

            sql = "INSERT INTO A_and_M " +
                    "VALUES " +
                    "(1, 'Fanchao Zhou', 24, 'ECEN')";
            test1Stmt.executeUpdate(sql);

            sql = "INSERT INTO A_and_M " +
                    "VALUES " +
                    "(2, 'Tian Xia', 25, 'CSCE')";
            test1Stmt.executeUpdate(sql);

            test1Stmt.close();
            test1Connection.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
