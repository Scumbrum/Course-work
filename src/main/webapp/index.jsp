<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@&display=swap" rel="stylesheet">
    <link rel="icon" type="image/png" href="Styles/multimedia/Logo.png">
    <link rel="stylesheet" type="text/css" href="Styles/S.css">
    <meta charset="UTF-8">
    <title>Authorisation</title>
</head>
<body class="body">
<div class= "authorisationPanel">
<h1 class="text" >Авторизуйтесь, будь ласка</h1>
<form class="authorisationForm" action="Signin" method="post">
    <p><input type="hidden" name="Signin" value="Signin"></p>
    <p class="authorisationParagraph" ><input class="input" type = "text" name = "login"/></p>
    <p class="authorisationParagraph"><input class="input" type = "password" name = "password"/></p>
    <c:set value="invalidcustomer" var="invalidcustomer"/>
    <c:if test="${!empty requestScope.exception && requestScope.exception==invalidcustomer}">
        <br>
        <hr class="line">
        <H2 class="warning">Невірні дані</H2>
        <hr class="line">
    </c:if>
    <p class="authorisationParagraph"><input class="button" type="submit" value="Ввійти"/></p>
</form>
<br>
</div>
<div class="underline">
<form class="authorisationForm" action="Signin" method="post">
    <input type="hidden" name="Signin" value="Checkin">
    <p class="authorisationParagraph"><input class="button" type="submit" value="Реєстрація"/></p>
</form>
    <p class="market">Тут може бути ваша реклама</p>
</div>
</body>
</html>