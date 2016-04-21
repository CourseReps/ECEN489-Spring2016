import java.sql.*;
import java.util.*;
import static java.lang.Math.*;
public class SQLiteJDBC
{
    public static void main(String[] args) {
        while (true) {
            Map<Double,String> avg = new HashMap<>();
            double rssimax = 0;
            String mac;
            Connection c = null;
            Statement stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:data.db");
                c.setAutoCommit(false);
                System.out.println("Database opened");

            } catch (Exception e) {
                System.err.println("An error occurred");
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(1);
            }
            try {
                String numentry = "SELECT COUNT(*) FROM wlan0";
                ResultSet numret = stmt.executeQuery(numentry);
                // get the number of rows from the result set
                numret.next();
                int rowCount = numret.getInt(1);
                System.out.println(rowCount);
                stmt.close();
                numret.close();
                for (int id = 0; id < rowCount; id++) {
                    String query = "SELECT MAC FROM wlan0 WHERE ID =" + id;
                    ResultSet macrs = stmt.executeQuery(query);
                    String macst = macrs.getString("MAC");
                    stmt.close();
                    macrs.close();

                    query = "SELECT * FROM wlan0 WHERE MAC=" + macst;
                    ResultSet rssi = stmt.executeQuery(query);
                    int rssiwlan0 = rssi.getInt("rssi");
                    stmt.close();
                    rssi.close();

                    query = "SELECT * FROM wlan1 WHERE MAC=" + macst;
                    rssi = stmt.executeQuery(query);
                    int rssiwlan1 = rssi.getInt("rssi");
                    stmt.close();
                    rssi.close();

                    query = "SELECT * FROM wlan2 WHERE MAC=" + macst;
                    rssi = stmt.executeQuery(query);
                    int rssiwlan2 = rssi.getInt("rssi");
                    stmt.close();
                    rssi.close();

                    double rssimag = sqrt((rssiwlan0 * rssiwlan0) + (rssiwlan1 * rssiwlan1) + (rssiwlan2 * rssiwlan2));

                    avg.put(rssimag,macst);
                }

            } catch (Exception e) {
                System.err.println("An error occurred");
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(2);
            }
            for (Map.Entry<Double, String> entry : avg.entrySet()) {
                double rssi = entry.getKey();
                if(rssi > rssimax){
                    rssimax=rssi;
                }
            }
            mac = avg.get(rssimax);
            System.out.println(mac);
            avg.clear();
            try {
                Thread.sleep(10000);
            }
            catch(Exception e) {
                System.err.println("An error occurred");
            }
        }
    }
}