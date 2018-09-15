package com.jwt.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.ArrayList;

public class MyServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		 
		if (request.getParameter("studentsButton") != null ) {
			doGet(request, response);
		}
		
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
		int totalCredits = 0;
		double totalGPA = 0;
		double finalGPA = 0;
		for (int i = 0; i<classNames.size(); i++) {
			out.print("</br>Class:   " + classNames.get(i)
					+ "</br>Credits: " + credits.get(i)
				    + "</br>GPA:     " + gpa.get(i) + "</br>");
			totalCredits += Integer.parseInt(credits.get(i));
			totalGPA += Integer.parseInt(credits.get(i)) * returnGPA(gpa.get(i));
		}
		out.print("</br>Final GPA: " + (totalGPA/totalCredits));
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
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//out.print("Driver loaded!");
		} catch (ClassNotFoundException e) {
			out.print("</br>Driver error: " + e);
		}
		
		try (Connection con = DriverManager.getConnection("jdbc:mysql://172.31.37.11:3306/gpaCalc", "gparemote", "password")) {
			Statement stmt = con.createStatement();
			
			//String sqlSort = "SELECT * FROM studentInfo ORDER BY NAME";
			//stmt.executeQuery(sqlSort);
			
			String sqlRetrieve = "SELECT NAME, CLASS, CREDITS, GPA FROM studentInfo ORDER BY NAME";
			ResultSet rs = stmt.executeQuery(sqlRetrieve);
			
			String oldName = "";
			while(rs.next()) {
				String name = rs.getString("NAME");
				String className = rs.getString("CLASS");
				String credits = rs.getString("CREDITS");
				String gpa = rs.getString("GPA");
				
				if (!name.equals(oldName)) {
					out.print("</br><b>Name: " + name + "</b></br>");
					oldName = name;
				}
				out.print("</br>Class:   " + className);
				out.print("</br>Credits: " + credits);
				out.print("</br>GPA:     " + gpa + "</br>");
			}
			
		} catch (Exception e2) {
			out.print("</br>Database error: " + e2);
		}
		out.close();
	}
	
	private static double returnGPA(String s) {
		switch(s) {
		case "A+":
		case "A":
			return 4.0;
		case "A-":
			return 3.67;
		case "B+":
			return 3.33;
		case "B":
			return 3.0;
		case "B-":
			return 2.67;
		case "C+":
			return 2.33;
		case "C":
			return 2.0;
		case "C-":
			return 1.67;
		case "D+":
			return 1.33;
		case "D":
			return 1;
		case "D-":
			return 0.67;
		case "F":
			return 0;
		}
		return 0.0;
	}
	
}