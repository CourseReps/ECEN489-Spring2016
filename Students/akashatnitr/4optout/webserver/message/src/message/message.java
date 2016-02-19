package message;

//package com.srccodes.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet implementation class HelloWorld
*/
@WebServlet("/message")
public class message extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 
  public message() {
      super();

  }
  static int index =1;
  String str_post = null;
  String str_get = null;
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

     // Set the response MIME type of the response message
     response.setContentType("text/html");
     // Allocate a output writer to write the response message into the network socket
     PrintWriter out = response.getWriter();

     // Write the response message, in an HTML page
     try {
//        out.println("<html>");
//        out.println("<head><title>Hello, World</title></head>");
//        out.println("<body>");
//        out.println("<h1>Hello, world!</h1>");  // says Hello
//        // Echo client's request information
//        out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
//        out.println("<p>Protocol: " + request.getProtocol() + "</p>");
//        out.println("<p>PathInfo: " + request.getPathInfo() + "</p>");
//        out.println("<p>Remote Address: " + request.getRemoteAddr() + "</p>");
        // Generate a random number upon each request
        out.println("GETreqNo: " + index + "");
        index++;
        
        handleRequest(request, response);

        //out.println("</body></html>");
     } finally {
        out.close();  // Always close the output writer
     }
     
  }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		

      String postData = request.getParameter("firstname");
      str_post = postData;
		PrintWriter gwriter = response.getWriter();
		try{
	        //gwriter.println("<html>");
//	        gwriter.println("<h1>");
//			gwriter.println("Sample Request Servlet");
//			gwriter.println("</h1>");
//			gwriter.println("<body>");
//			gwriter.println("<p>");
//			gwriter.println("<br><br>");
	        gwriter.print("Request : ");
	        gwriter.println(index);
	        gwriter.print(" PostString: ");
	       // gwriter.print(postData);
	       // gwriter.println("<br>");
	        handleRequest(request, response);
//	        gwriter.println("</p>");
//	        gwriter.println("</body>");
//	        gwriter.println("</html>");
	        index++;
	        

			
			 
	        }
	        catch(Exception e)
	        {
	        	gwriter.println("<html>");
	             gwriter.println("<head>");
	     		gwriter.println("<title>Sample Request Servlet</title>");
	     		gwriter.println("</head>");
	     		gwriter.println("<body>");
	     		gwriter.println("<p>");
	     		gwriter.println("<br><br>");
	             gwriter.println("Request : ");
	           //  gwriter.println(index);
	             gwriter.println("String: Error Response ");
	             gwriter.println(str_post);
	             gwriter.println("<br>");
	             gwriter.println("</p>");
	             gwriter.println("</body>");
	             gwriter.println("</html>");
	          //   index++;
	             

	        	
	        }
	}    
	
	 public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
		 
		  
		  
		         PrintWriter out = res.getWriter();
		 
		         res.setContentType("text/html");
		 
		  
		 
		         Enumeration<String> parameterNames = req.getParameterNames();
		 
		  
		 
		         while (parameterNames.hasMoreElements()) {
		 
		  
		 
		             String paramName = parameterNames.nextElement();
		 
		             out.write(paramName);
		 
		             out.write(":");
	 
		  
		 
		             String[] paramValues = req.getParameterValues(paramName);
		 
		             for (int i = 0; i < paramValues.length; i++) {
		 
		                 String paramValue = paramValues[i];
		  
		                 out.write("" + paramValue);
		 
		                 out.write(" ");
		 
		             }
		  
		  
		 
		         }
		         String currTime = ( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		         out.write(currTime);
		  
		  
		 
		         out.close();
		 
		  
		 
		     }

  
  
  
  
}

//}