/**
 * Created by Chaance on 1/29/2016.
 */


import java.sql.*;

public class JavaJDBC
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
            String sql = "CREATE TABLE IF NOT EXISTS ROSTER " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " AGE            INT     NOT NULL, " +
                    " POSITION       TEXT    NOT NULL)";
            test1Stmt.executeUpdate(sql);

            sql = "INSERT INTO ROSTER " +
                    "VALUES " +
                    "(1, 'Chaance Graves', 22, 'OL')";
            test1Stmt.executeUpdate(sql);

            sql = "INSERT INTO ROSTER " +
                    "VALUES " +
                    "(2, 'John Brown', 25, 'RB')";
            test1Stmt.executeUpdate(sql);

            test1Stmt = test1Connection.createStatement();
            sql = "DELETE from ROSTER where ID=2;";
            test1Stmt.executeUpdate(sql);
            test1Connection.commit();

            test1Stmt.close();
            test1Connection.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
