<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Authorisation</title>
</head>
<body>
<h1>Авторизуйтесь будь ласка</h1>
<form action="Signin" method="post">
    <input type="hidden" name="Signin" value="Signin">
    <input type = "text" name = "login"/>
    <br>
    <input type = "password" name = "password"/>
    <br>
    <%
        if(request.getAttribute("exception")!=null && request.getAttribute("exception").equals("invalidcustomer")){
    %>
    <H2>Невірні дані</H2>
    <%
        }
    %>
    <input type="submit" value="Ввійти"/>
</form>
<br>
<form action="Signin" method="post">
    <input type="hidden" name="Signin" value="Checkin">
    <input type="submit" value="Реєстрація"/>
</form>
</body>
</html>