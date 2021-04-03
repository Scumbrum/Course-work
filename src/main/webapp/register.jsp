<%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 27.03.2021
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
<form action="DoRegister" method="post">
    <p>Придумайте логін <input type="text" name="login"/></p>
    <p>Введіть пароль <input type="password" name="password1"/></p>
    <p>Підтвердіть пароль <input type="password" name="password2"/></p>
    <%
        if(request.getAttribute("exception")!=null && request.getAttribute("exception").equals("notsame")){
            %>
    <p>Паролі не сходяться</p>
    <%
      } else if(request.getAttribute("exception")!=null && request.getAttribute("exception").equals("existlogin")){
    %>
    <p>Існуючий логін</p>
    <%
        }
    %>
    <input type="submit" value="Register"/>
</form>
</body>
</html>
