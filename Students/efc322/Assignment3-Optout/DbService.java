import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by Fanchao Zhou on 2/11/2016.
 */

public class DbService extends HttpServlet {
    private String welcomeLine;
    private int clientTotalCnt;

    @Override
    public void init(){
        clientTotalCnt = 0;
        welcomeLine = "Welcome To Fanchao's First Server! ";
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){

        try{
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            ++clientTotalCnt;
            out.println("<h1>" + request.getParameter("Name")+ ", " + welcomeLine +
                    clientTotalCnt + " clients(including you) have visited this server. " + "</h1>");
        }catch(Exception e){
            System.err.println(e);
        }
    }
}
