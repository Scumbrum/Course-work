<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@&display=swap" rel="stylesheet">
    <link rel="icon" type="image/png" href="Styles/multimedia/Logo.png">
    <link rel="stylesheet" type="text/css" href="Styles/S.css">
    <title>Register</title>
    <meta charset="UTF-8">
</head>
<body class="body">
<div class="authorisationPanel">
<form class="authorisationForm" action="Signin" method="post">
    <table><tr><td class="notation">Придумайте логін: </td>
    <td class="right"> <input class="input" type="text" name="login"/> </td>
    </tr>
        <tr>
        <td class="notation">Введіть пароль:</td>
        <td class="right"><input class="input" type="password" name="password1"/></td>
    </tr>
        <tr>
            <td class="notation">Підтвердіть пароль:</td>
            <td class="right"><input class="input" type="password" name="password2"/></td>
        </tr>
    </table>
    <c:set value="notsame" var="notsame"/>
    <c:set value="empty" var="loginEmpty"/>
    <c:set value="existlogin" var="existlogin"/>
    <c:if test="${!empty requestScope.exception}">
        <hr class="line">
        <c:if test="${requestScope.exception==notsame}">
            <p class="warning">Паролі не сходяться</p>
        </c:if>
        <c:if test="${requestScope.exception==loginEmpty}">
            <p class="warning">Пустий логін</p>
        </c:if>
        <c:if test="${requestScope.exception==existlogin}">
            <p class="warning">Існуючий логін</p>
        </c:if>
        <hr class="line">
    </c:if>
    <input type="hidden" name="Signin" value="Checkin"/>
    <input class="button" type="submit" value="Register"/>
</form>
</div>
<div class="underline">

</div>
</body>
</html>
