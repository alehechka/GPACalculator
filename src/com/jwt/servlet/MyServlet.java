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
public class MyServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		 
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String className[] = new String[5], credits[] = new String[5], gpa[] = new String[5];
		
		String name = request.getParameter("userName");
		for (int i = 0; i<5; i++) {
			className[i] = request.getParameter("className" + i);
			credits[i] = request.getParameter("credits" + i);
			gpa[i] = request.getParameter("gpa" + i);
		}
		out.print("Name: " + name 
				+ "\nClass: " + className
				+ "\nCredits: " + credits[0]
				+ "\nGPA: " + gpa[0]);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://172.31.37.11:3306/gpaCalc", "gparemote", "password");

			PreparedStatement ps = con.prepareStatement("insert into studentInfo values(?,?,?,?)");
			int n = 0;
			for(int i = 0; i<5; i++) {
				ps.setString(1, name);
				ps.setString(2, className[i]);
				ps.setString(3, credits[i]);
				ps.setString(4, gpa[i]);

				n = ps.executeUpdate();
			}
			if (n > 0)
				out.print("You are successfully registered...");
			else
				out.print("Something went wrong");

		} catch (Exception e2) {
			System.out.println(e2);
		}
		out.close();
	}
}