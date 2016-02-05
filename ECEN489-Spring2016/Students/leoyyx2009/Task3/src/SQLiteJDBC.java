/**
 * Created by YYX on 1/29/16.
 * ECEN689-610:SPTP: DATA ACQ EMBEDDED SYS
 * Auther:  Yanxiang Yang
 * Task3
 */

// -----------------------------------------------------------------
// imports
import java.sql.*;
// create a class named SQLite to connect to SQLite data base and manage a table

public class SQLiteJDBC
{
    public static void main( String args[] ) {

        Connection c = null;

        Statement stmt = null;

        try {
            // initialize the connection
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            String sql = "CREATE TABLE ECEN689" +
                    "(ID INT PRIMARY KEY   NOT NULL," +
                    " NAME           TEXT  NOT NULL)";
            stmt.executeUpdate(sql);

            // insert something into table
            sql = "INSERT INTO ECEN689 (ID, NAME)" +
                    " VALUES (1,'YANXIANG YANG');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO ECEN689 (ID, NAME)" +
                    " VALUES (2,'YANXIANG YANG');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO ECEN689 (ID, NAME)" +
                    " VALUES (3,'YANXIANG YANG');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO ECEN689 (ID, NAME)" +
                    " VALUES (4,'YANXIANG YANG');";
            stmt.executeUpdate(sql);

            // fetch and display records from the table created
            ResultSet rs = stmt.executeQuery ("SELECT * FROM ECEN689;");
            while ( rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println ("ID = " + id );
                System.out.println ("NAME = " + name);

            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfuly");
    }
}