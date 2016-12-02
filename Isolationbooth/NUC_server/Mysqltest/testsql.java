import java.sql.*;
import java.util.*;
public class testsql{
public static void main(String[] args){
String url = "jdbc:mysql://localhost:3306/data";
String username = "root";
String password = "isobooth";

System.out.println("Connecting database...");
try{
Class.forName("com.mysql.jdbc.Driver");
 System.out.println("driver loaded!");
}catch (ClassNotFoundException e){
throw new IllegalStateException("Cannot find the driver in the classpath!", e);
}

try(Connection connection = DriverManager.getConnection(url, username, password)) {
	System.out.println("Database connected!");
} catch (SQLException e){
	throw new IllegalStateException("Cannot connect the databse!", e);
}
}
}
