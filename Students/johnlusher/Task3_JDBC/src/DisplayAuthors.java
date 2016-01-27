// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		DisplayAuthors.java
// Version:			1.0.0
// Date:			January 26, 2016
// Description:	    Task #3 - Java JDBC
// Author:          John Lusher II
// ---------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Revision(s):     Date:                       Description:
//  v1.0.0           January 26, 2016  	         Initial Release
//  --------------------------------------------------------------------------------------------------------------------


//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
import com.sun.corba.se.impl.orb.DataCollectorBase;
import java.sql.*;

//  --------------------------------------------------------------------------------------------------------------------
//        Class:    DisplayAuthors
//  Description:	DisplayAuthors class for project
//  --------------------------------------------------------------------------------------------------------------------
public class DisplayAuthors
{
    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     Class Construction
    //      Inputs:	    arguments
    //     Outputs:	    none
    //  Description:    Defines and initializes the class: main
    //	----------------------------------------------------------------------------------------------------------------
    public static void main(String[] args)
    {
        Connection c = null;
        Statement stmt = null;

        try
        {
            // Create class interface and get connection to the books database
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:books.db");
            c.setAutoCommit(false);

            // Print out a debug statement to let us know we have opened the database
            System.out.println("Opened database successfully");

            // Create a statement
            stmt = c.createStatement();

            // Execute a query to select the three columns data from the authors table
            ResultSet rs = stmt.executeQuery( "SELECT authorID, firstName, lastName FROM authors;" );

            // Loop through all the returned records, until EOF
            while ( rs.next() )
            {
                // Capture the data from the record set
                int authorID = rs.getInt("authorID");
                String  firstName = rs.getString("firstName");
                String  lastName = rs.getString("lastName");

                // Print out the data to the debug console
                System.out.println( "  AuthorID: " + authorID );
                System.out.println( "First Name: " + firstName );
                System.out.println( " Last Name:" + lastName );
                System.out.println();
            }

            // Close the record set,  statement, and connection
            rs.close();
            stmt.close();
            c.close();
        }
        catch ( Exception e )
        {
            // Print the exception data and exit
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        // Test completed successfully
        System.out.println("Operation done successfully");
    }
}
