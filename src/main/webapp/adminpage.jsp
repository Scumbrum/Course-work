<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="AdminManipulation.ChannelController" %><%--
  Created by IntelliJ IDEA.
  User: UserS
  Date: 29.03.2021
  Time: 13:28
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@&display=swap" rel="stylesheet">
    <link rel="icon" type="image/png" href="Styles/multimedia/Logo.png">
    <link rel="stylesheet" type="text/css" href="Styles/S.css">
    <meta charset="UTF-8">
    <title>AdminPage</title>
</head>
<body class="body">
<header class="customHeader">
    <div class="name">
        <pre class="name">Teleprogram</pre>
    </div>
    <div class="name">
        <pre class="name">Hello, ${sessionScope.login}</pre>
    </div>
    <div class="quitPanel">
        <form class="quitForm" action="Signin" method="post">
            <input class="quit" type="Submit" name="action" value="Quit"/>
        </form>
    </div>
</header>
<div class="workPanel">
<jsp:include page="WEB-INF/jspf's/adminPanel.html"/>

<c:if test="${!empty requestScope.controller}">

    <c:set var="channels" scope="page" value="channels"/>
    <c:if test="${requestScope.controller==channels}">

        <jsp:include page="WEB-INF/jspf's/channelSettings.jsp"/>

    </c:if>

    <c:set var="transfers" scope="page" value="transfers"/>
    <c:if test="${requestScope.controller==transfers}">

        <jsp:include page="WEB-INF/jspf's/transferSettings.jsp"/>

    </c:if>

    <c:set var="program" scope="page" value="program"/>
    <c:if test="${requestScope.controller==program}">
        <jsp:include page="WEB-INF/jspf's/programSettings.jsp"/>
    </c:if>
</c:if>
    <jsp:include page="WEB-INF/jspf's/adminException.jsp"/>
</div>
</body>
</html>
