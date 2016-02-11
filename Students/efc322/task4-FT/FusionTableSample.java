
/*
 * Make sure to add the proper JAR files to the classpath in order to import the API classes!
 * They can be found in the Fusion Table API Client library for Java. Links to all files are
 * located on the Fusion Tables tutorial page. I added the following to my classpath:
 * \fusiontables\libs
 * google-api-services-fusiontables-v2-rev1-1.19.1.jar
 * servlet-api.jar
*/

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.Fusiontables.Table.Delete;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Table;
import com.google.api.services.fusiontables.model.TableList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class FusionTableSample {

    /**
     * Be sure to specify the name of your application. I set mine to match my project name on the Google dev console
     */
    private static final String APPLICATION_NAME = "App1";

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.dir"), ".store/fusion_tables_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static Fusiontables fusiontables;

    public static void main(String[] args) {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();
            // set up global FusionTables instance
            fusiontables = new Fusiontables.Builder(
                    httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
            final String[] colNames = {"Name", "Id", "City"};
            final String tableName="Table1";

            String[] newTblCols= {colNames[ 0 ], "STRING", colNames[ 1 ], "NUMBER", colNames[ 2 ], "LOCATION"};
            createTable(tableName, newTblCols);     //Create a new table
            String[] tableNames = listTables();    //List the name of every table
            String[] colValues = {"Dayang", "1", "Austin"};
            insertRow(getTableId(tableName), colNames, colValues);//Insert a record into the table
            colValues[ 0 ] = "Fanchao";
            colValues[ 1 ] = "2";
            colValues[ 2 ] = "College Station";
            insertRow(getTableId(tableName), colNames, colValues);//Insert a record into the table
            colValues[ 0 ] = "Ao";
            colValues[ 1 ] = "3";
            colValues[ 2 ] = "New York";
            insertRow(getTableId(tableName), colNames, colValues);//Insert a record into the table
            showRows(getTableId(tableName), "*");                 //Show all the records in a table

            return;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }

    /*  Method that, when run for the first time, authorizes the installed application
     *  to access user's protected data. It creates a credentials file that will be stored in the location
     *  specified by the DATA_STORE_DIR variable. It is also NECESSARY to ensure the proper JSON object containing
     *  the client ID, client secret, and redirect URIs is located in the project src directory! Just download the JSON
     *  from the dev console and cut/paste directly into the src directory. Don't modify any fields in the JSON.
     */
    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new InputStreamReader(
                        FusionTableSample.class.getResourceAsStream("/client_secrets.json")));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                    "Enter Client ID and Secret from https://code.google.com/apis/console/?api=fusiontables "
                            + "into fusiontables-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(FusiontablesScopes.FUSIONTABLES)).setDataStoreFactory(
                dataStoreFactory).build();

        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private static String[] listTables() throws IOException {
        View.header("Listing My Tables");

        // Fetch the table list
        List<Table> tableList = fusiontables.table().list().execute().getItems();
        String[] tableNames=new String[ tableList.size() ];

        if (tableList == null || tableList.isEmpty()) {
            System.out.println("No tables found!");
            return null;
        }

        int cnt;
        for (cnt = 0; cnt < tableList.size(); cnt++) {
            View.show(tableList.get(cnt));
            tableNames[ cnt ] = tableList.get(cnt).getName();
            View.separator();
        }

        return tableNames;
    }

    private static String getTableId(String tableName) throws IOException {
        View.header("Getting TableID for " + tableName);

        // Fetch the table list
        Fusiontables.Table.List listTables = fusiontables.table().list();
        TableList tablelist = listTables.execute();

        if (tablelist.getItems() == null || tablelist.getItems().isEmpty()) {
            System.out.println("No tables found!");
            return null;
        }

        String tid = null;
        for (Table table : tablelist.getItems()) {
            if (table.getName().equals(tableName))
                tid = table.getTableId();
        }
        System.out.println(tableName + " - tableId: " + tid);
        return tid;
    }

    private static void showRows(String tableId, String... colsName) throws IOException {
        View.header("Showing Rows From Table");

        String query = "SELECT ";
        String tempStr;
        int count;

        if(colsName.length == 0){
            query = query+"* FROM "+tableId;
            Sql sql = fusiontables.query().sql(query);
            try {
                sql.executeAndDownloadTo(System.out);
            } catch (IllegalArgumentException e){
            }

            return;
        }

        for(count = 0; count < colsName.length; count++){
            query = query+colsName[ count ];
            if(count<colsName.length-1){
                query = query+",";
            }
        }

        query = query+" FROM "+tableId;
        Sql sql = fusiontables.query().sql(query);
        try {
            sql.executeAndDownloadTo(System.out);
        } catch (IllegalArgumentException e) {
        }
    }

    private static String createTable(String tableName, String[] colNames) throws IOException {
        if(colNames.length%2 != 0){
            System.out.println("The Names and Types do NOT match");
            return null;
        }

        View.header("Create Sample Table");

        // Create a new table
        Table table = new Table();
        table.setName(tableName);
        table.setIsExportable(false);

        // Set columns for new table
        ArrayList<Column> newTblCol = new ArrayList<Column>();
        int cnt;
        for(cnt = 0; cnt < colNames.length; cnt += 2){
            newTblCol.add(new Column().setName(colNames[ cnt ]).setType(colNames[ cnt+1 ]));
        }

        // Adds a new column to the table.
        table.setColumns(newTblCol);
        Fusiontables.Table.Insert t = fusiontables.table().insert(table);
        table = t.execute();

        View.show(table);

        return table.getTableId();
    }

    private static void insertRow(String tableId, String[] colNames, String[] colValues) throws IOException {
        if(colNames==null || colValues==null || colNames.length!=colValues.length){
            System.out.println("Invalid column names or types!");
            return;
        }

        //You can change the text and data being inserted into the table by following the SQL syntax
        String query = "INSERT INTO " + tableId + " (";
        int count;
        for(count = 0; count < colNames.length; count++){
            query = query+colNames[ count ];
            if(count < colNames.length-1){
                query = query+",";
            }
        }
        query = query+") VALUES (";
        for(count = 0; count < colNames.length; count++){
            query = query+"'"+colValues[ count ]+"'";
            if(count < colNames.length-1){
                query = query+", ";
            }
        }
        query = query+")";

        Sql sql = fusiontables.query().sql(query);

        try {
            sql.execute();
        } catch (IllegalArgumentException e) {
        }
    }

    private static void deleteRow(String tableId, int rowId) throws IOException {

        String deleteCmd = "DELETE FROM " + tableId;

        if(rowId >= 1){
            deleteCmd += " WHERE ROWID="+"'"+rowId+"'";
        }

        Sql sql = fusiontables.query().sql(deleteCmd);
        try {
            sql.execute();
        } catch (IllegalArgumentException e) {
        }
    }
}
