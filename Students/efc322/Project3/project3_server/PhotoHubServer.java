import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by Fanchao Zhou on 4/20/2016.
 */
public class PhotoHubServer extends HttpServlet{

    private static final String paraName = "photo data";
    private static final String keyUpload = "keyTypeUpload";
    private static final String keyDownload = "keyTypeDownload";
    private static final String keyType = "keyType";
    private static final String keyName = "keyName";
    private static final String keyFileName = "keyFileName";
    private static final String keyImage = "keyImage";
    private static final String imgFolderName = "project3_img";
    private static final String dbName = "ecen489_project3";
    private static final String tbName = "recordtable";
    private static final String tbNameColumn = "name";
    private static final String tbPhotoDirColumn = "photo_file_name";
    Connection conn = null;
    Statement stmt = null;

    @Override
    public void init() {

        try{
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Properties props = new Properties();
            props.put("user", "Fanchao");
            props.put("password", "ZFC_mysql2016");
            props.put("useSSL", "true");
            conn = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/"+dbName, props);//Use the Driver Manager to set up the driver of a database
            stmt = conn.createStatement();
            conn.setAutoCommit(false);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        try {
            String jsonData = req.getParameter(paraName);
            Gson gson = new GsonBuilder().create();
            JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
            String type = jsonObject.get(keyType).getAsString();

            if(type.equals(keyUpload)){
                String imageString = jsonObject.get(keyImage).getAsString(); // returns a JsonElement for that name
                String imageFileName = jsonObject.get(keyFileName).getAsString();
                String name = jsonObject.get(keyName).getAsString();

                resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();
                out.print("");
                out.close();

                byte[] imageByteArray = Base64.getUrlDecoder().decode(imageString);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                imageFileName = imageFileName+(dateFormat.format(new Date()))+".jpg";
                File file = new File(imgFolderName, imageFileName);
                FileOutputStream fileOS = new FileOutputStream(file);
                fileOS.write(imageByteArray);
                fileOS.close();

                String sqlInsert = "INSERT INTO "+tbName +
                        " (" + tbNameColumn + ", " + tbPhotoDirColumn + ") values ("+
                        "\'" + name + "\'" + "," + "\'" + imageFileName + "\'" + ");";
                stmt.executeUpdate(sqlInsert);
                conn.commit();
            }
        } catch ( Exception e ) {
            if(conn != null){
                try{
                    conn.rollback();
                } catch(Exception errRollBack){
                    System.out.println(errRollBack);
                }
            }
            System.out.println(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        try{
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}