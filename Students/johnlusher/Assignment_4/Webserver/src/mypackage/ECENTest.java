/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mypackage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Simple servlet to validate that the Hello, World example can
 * execute servlets.  In the web application deployment descriptor,
 * this servlet must be mapped to correspond to the link in the
 * "index.html" file.
 *
 * @author Craig R. McClanahan <Craig.McClanahan@eng.sun.com>
 */

public final class ECENTest extends HttpServlet {

    private static final long serialVersionUID = 1L;
	private static String st_posted = "";
	private static int index = 1;

    /**
     * Respond to a GET request for the content produced by
     * this servlet.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are producing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
      throws IOException, ServletException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {

            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<title>ECEN689 - Sample Servlet Page</title>");
            writer.println("</head>");
            writer.println("<body>");


            writer.println("<div style=\"float: left; padding: 10px;\">");
            writer.println("<img src=\"images/astar.jpg\" alt=\"\" />");
            writer.println("</div>");
            writer.println("<h1>Sample Application Servlet</h1>");
            writer.println("<p>");
            writer.println("Hello! Is it me you're looking for?");
            writer.println("<br><br><br><br>");
            writer.println("Get Request #: ");
            writer.println(index);
            writer.println("Updated String: ");
            writer.println(st_posted);
            writer.println("<br><br><br><br>");
            writer.println("</p>");

            writer.println("</body>");
            writer.println("</html>");
			index++;
        }
    }

    /**
     * Respond to a GET request for the content produced by
     * this servlet.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are producing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
      throws IOException, ServletException {

        String posteddata = request.getParameter("posteddata");
		st_posted = posteddata;
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {

            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<title>ECEN689 - Sample Servlet Page</title>");
            writer.println("</head>");
            writer.println("<body>");


            writer.println("<div style=\"float: left; padding: 10px;\">");
            writer.println("<img src=\"images/astar.jpg\" alt=\"\" />");
            writer.println("</div>");
            writer.println("<h1>Sample Application Servlet</h1>");
            writer.println("<p>");
            writer.println("Hello! Is it me you're looking for?");
            writer.println("Index: ");
            writer.println(index);
            writer.println("Name: ");
            writer.println(st_posted);
            writer.println("<br><br><br><br>");
            writer.println("</p>");

            writer.println("</body>");
            writer.println("</html>");
        }
    }


}
