<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<body>
    <form action="register" method="post">
 
        Name:<input type="text" name="userName"/><br/>
        <br/>
        <c:forEach var="i" begin="1" end="5">
        	Class: <input type="text" name="className${count}"/><br/>
        	Credits: <select name="credits${count}">
        		<option>0</option>
        		<option>1</option>
        		<option>2</option>
        		<option>3</option>
        		<option>4</option> 
        		<option>5</option>
        		<option>6</option>
        	</select> <br/>
        	GPA: <select name="gpa${count}">
        		<option>A+</option>
        		<option>A</option>
        		<option>A-</option>
        		<option>B+</option>
        		<option>B</option>
        		<option>B-</option>
        		<option>C+</option>
        		<option>C</option>
        		<option>C-</option>
        		<option>D</option>
        		<option>F</option>
        		<option>NA</option>
        	</select> <br/>
        	<br/>
        </c:forEach>

        <input type="submit" value="Submit"/>
 
    </form>
</body>
</html>