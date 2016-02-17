package com.tbranyon.optout4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WebServerOO4
 */
@WebServlet("/WebServerOO4")
public class WebServerOO4 extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private ArrayList<ArrayList<String>> entries;
	
	public WebServerOO4()
	{
		super();
		entries = new ArrayList<ArrayList<String>>();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html");
		PrintWriter os = response.getWriter();
		os.println("<h1>Hello!</h1>");
		os.println("<table border=\"1\">");
		os.println("<tr>");
		os.println("<th>Timestamp</th><th>IP Address</th><th>Android Version</th></tr>");
		for(ArrayList<String> entry : entries)
		{
			String timestamp = entry.get(0);
			String IP = entry.get(1);
			String version = entry.get(2);
			os.println("<tr>");
			os.println("<td>" + timestamp + "</td>");
			os.println("<td>" + IP + "</td>");
			os.println("<td>" + version + "</td>");
			os.println("</tr>");
		}
		os.println("</table>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//doGet(request, response); ///???
		//parse data and insert into ArrayList
		System.out.println("POST received");
		BufferedReader is = request.getReader();
		String json = is.readLine();
		is.close();
		int IPindex = json.indexOf("IP")+5;
		int versionIndex = json.indexOf("Android")+17;
		int timeIndex = json.indexOf("Time")+12;
		String IP = json.substring(IPindex,json.indexOf("\"", IPindex));
		String version = json.substring(versionIndex,json.indexOf("\"",versionIndex));
		String time = json.substring(timeIndex,json.indexOf("\"",timeIndex));
		ArrayList<String> thisEntry = new ArrayList<String>();
		thisEntry.add(time);
		thisEntry.add(IP);
		thisEntry.add(version);
		entries.add(thisEntry);
	}

}
