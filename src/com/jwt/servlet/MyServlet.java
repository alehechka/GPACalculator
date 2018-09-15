package com.jwt.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class MyServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		 
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		ArrayList<String> classNames = new ArrayList<String>();
		ArrayList<String> credits = new ArrayList<String>();
		ArrayList<String> gpa = new ArrayList<String>();
		
		
		String name = request.getParameter("userName");
		String className = "";
		
		for (int i = 0; i<5; i++) {
			if((className=request.getParameter("className" + (i+1))).equals("") || classNames.contains(className))
				continue;
			classNames.add(className);
			credits.add(request.getParameter("credits" + (i+1)));
			gpa.add(request.getParameter("gpa" + (i+1)));
		}
		out.print("Name: " + name + "</br>");
		for (int i = 0; i<classNames.size(); i++) {
			out.print("</br>Class: " + classNames.get(i)
					+ "</br>Credits: " + credits.get(i)
				    + "</br>GPA: " + gpa.get(i) + "</br>");
		}
		//out.print("Loading driver...");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//out.print("Driver loaded!");
		} catch (ClassNotFoundException e) {
			out.print("</br>Driver error: " + e);
		}
		
		//out.print("Connecting database...");
	
		try (Connection con = DriverManager.getConnection("jdbc:mysql://172.31.37.11:3306/gpaCalc", "gparemote", "password")) {

			PreparedStatement ps = con.prepareStatement("insert into studentInfo values(?,?,?,?)");
			int n = 0;
			for(int i = 0; i<classNames.size(); i++) {
				ps.setString(1, name);
				ps.setString(2, classNames.get(i));
				ps.setString(3, credits.get(i));
				ps.setString(4, gpa.get(i));

				n = ps.executeUpdate();
			}
			//if (n > 0)
			//	out.print("You are successfully registered...");
			//else
				//out.print("Something went wrong");

		} catch (Exception e2) {
			out.print("</br>Database error: " + e2);
		}
		out.close();
	}
}