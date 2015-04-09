<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>

<body>
	This is my JSP page.
	<br>


	<!--  这个 GET请求 注释了  
    <p>
        以GET方法发送：<br>
         
    <form action="servlet/WelcomeUserServlet" method="get">
        Username: <input type="text" name="username" value="">
        Age: <input type="text" name="age" value="">
        <input type="submit" value="Submit">
    </form>
    </p>
    -->

	<p>
		以POST方法发送：<br>
	<form action="servlet/WelcomeUserServlet" method="post">
		Username: <input type="text" name="username" value="">
		Password: <input type="password" name="password" value=""> <input type="submit" value="Submit">
	</form>
	<p>

</body>
</html>
