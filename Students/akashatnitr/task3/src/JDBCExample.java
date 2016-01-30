package jdbc;
import java.io.File;
import java.sql.*;

 

public class JDBCExample
{
	public static Connection c = null;
	public  static Statement stmt = null;
  public static void main( String args[] )
  {
	  createDBase();
	  createRecord();
	  //deleteRecords();
	  viewRecords();
    
    
    //System.out.println("Opened database successfully");
  }
  
  public static void createDBase()
  {
	  String dbName = "test.db";
	  File file = new File (dbName);

	  if(file.exists()) //here's how to check
	     {
	         System.out.println("This database name already exists. Not creating Again");
	     }
	     else{

	          
	    
	  
	  
	  
	    try {
	    	Class.forName("org.sqlite.JDBC");
	        c = DriverManager.getConnection("jdbc:sqlite:test.db");
	    
	        stmt = c.createStatement();
	        
	        DatabaseMetaData md = c.getMetaData();
	        ResultSet rs = md.getTables(null, null, "%", null);
	        while (rs.next()) {
	          System.out.println("table"+rs.getString(3));
	        }
	        
	        String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
	                     "(ID INT PRIMARY KEY     NOT NULL," +
	                     " NAME           TEXT    NOT NULL, " + 
	                     " AGE            INT     NOT NULL, " + 
	                     " ADDRESS        CHAR(50), " + 
	                     " SALARY         REAL)"; 
	        stmt.executeUpdate(sql);
	        stmt.close();
	        c.close();
	        System.out.println(" table created successfully");

	        
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      
	     // System.exit(0);
	    }
	     }
  }
  
  public static void createRecord()
  {
	  try {
	        Class.forName("org.sqlite.JDBC");
	        c = DriverManager.getConnection("jdbc:sqlite:test.db");
	        c.setAutoCommit(false);
	       // System.out.println("Opened database successfully");

	        stmt = c.createStatement();
	        String sql = null;
	       /*  sql = "INSERT OR REPLACE INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
	                    "VALUES (( SELECT ID FROM COMPANY WHERE ID = 1), 'Akash', 32, 'California', 210000.00 );";
	        //System.out.println("yeah");
	        stmt.executeUpdate(sql);
	        
	         */

	        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
	              "VALUES (2, 'Akash', 25, 'Texas', 15000.00 );"; 
	        stmt.executeUpdate(sql);

	        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
	              "VALUES (3, 'John', 23, 'NYC', 20000.00 );"; 
	        stmt.executeUpdate(sql);

	        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
	              "VALUES (4, 'Doe', 25, 'Bombay ', 65000.00 );"; 
	        stmt.executeUpdate(sql);

	        stmt.close();
	        c.commit();
	        c.close();
	        System.out.println("Records created successfully");
	      } catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        //System.exit(0);
	      }
  }
  
  public static void viewRecords(){
  try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:test.db");
      c.setAutoCommit(false);
     // System.out.println("Opened database successfully");

      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
      while ( rs.next() ) {
         int id = rs.getInt("id");
         String  name = rs.getString("name");
         int age  = rs.getInt("age");
         String  address = rs.getString("address");
         float salary = rs.getFloat("salary");
         System.out.println( "ID = " + id );
         System.out.println( "NAME = " + name );
         System.out.println( "AGE = " + age );
         System.out.println( "ADDRESS = " + address );
         System.out.println( "SALARY = " + salary );
         System.out.println("Displayed records ");
      }
      rs.close();
      stmt.close();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      //System.exit(0);
    }
  }
  
  
  public static void deleteRecords()
  {
 
  
  try {
	  String delString = "DELETE FROM COMPANY;";
	  Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:test.db");
      c.setAutoCommit(false);
     // System.out.println("Opened database successfully");

      stmt = c.createStatement();
      stmt.executeUpdate(delString);
      //rs.close();
      //stmt.executeUpdate(sql);

      stmt.close();
      c.commit();
      c.close();
	
} catch ( Exception e ) {
    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    //System.exit(0);
  } 
  }
  
}



//class JDBCExample{
//	public static void main(String[] args) {
//		//set connection
//		
//		try {
//			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecen689db","akashmysql","akash123");
//			System.out.println("Connection done");
//			Statement st = connection.createStatement();
//			ResultSet rs  = st.executeQuery("SELECT * FROM salespeople");
//			while(rs.next())
//			{
//				System.out.println(rs.getInt("id")+" "+ rs.getString("s_name")+" "+ rs.getString("s_city")+" "+ rs.getFloat("comm"));
//			}
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		//create startement
//		//ex query
//		//result set
//		
//	}
//}






//
////STEP 1. Import required packages
//import java.sql.*;
//
//public class JDBCExample {
// // JDBC driver name and database URL
// static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
// static final String DB_URL = "jdbc:mysql://localhost/";
//
// //  Database credentials
// static final String USER = "root@localhost";
// static final String PASS = "root123";
// 
// public static void main(String[] args) {
// Connection conn = null;
// Statement stmt = null;
// try{
//    //STEP 2: Register JDBC driver
//    Class.forName("com.mysql.jdbc.Driver");
//
//    //STEP 3: Open a connection
//    System.out.println("Connecting to database...");
//    conn = DriverManager.getConnection(DB_URL, USER, PASS);
//
//    //STEP 4: Execute a query
//    System.out.println("Creating database...");
//    stmt = conn.createStatement();
//    
//    String sql = "CREATE DATABASE STUDENTS";
//    stmt.executeUpdate(sql);
//    System.out.println("Database created successfully...");
// }catch(SQLException se){
//    //Handle errors for JDBC
//    se.printStackTrace();
// }catch(Exception e){
//    //Handle errors for Class.forName
//    e.printStackTrace();
// }finally{
//    //finally block used to close resources
//    try{
//       if(stmt!=null)
//          stmt.close();
//    }catch(SQLException se2){
//    }// nothing we can do
//    try{
//       if(conn!=null)
//          conn.close();
//    }catch(SQLException se){
//       se.printStackTrace();
//    }//end finally try
// }//end try
// System.out.println("Goodbye!");
//}//end main
//}//end JDBCExample