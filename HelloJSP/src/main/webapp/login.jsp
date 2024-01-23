<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	//Lấy dữ liệu từ người dùng
	String tenDN = request.getParameter("email");
	String pass = request.getParameter("pswd");
	if(tenDN.equals("ABC@gmail.com") && pass.equals("MNK"))
		response.sendRedirect("UserProfile.html");
	else 
		response.sendRedirect("Login.html");
%>
</body>
</html>