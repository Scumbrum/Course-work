<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.TreeSet" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 29.03.2021
  Time: 13:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@&display=swap" rel="stylesheet">
    <link rel="icon" type="image/png" href="Styles/multimedia/Logo.png">
    <link rel="stylesheet" type="text/css" href="Styles/S.css">
    <title>ClientPage</title>
</head>
<body class="body">
<c:if test="${!empty requestScope.exception}">
<script type="application/javascript">
    alert("${requestScope.exception}")
</script>
</c:if>
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
<c:choose>
    <c:when test="${empty requestScope.showlist}">
        <c:redirect url="ClientView"/>
    </c:when>
    <c:otherwise>

        <jsp:include page="WEB-INF/jspf's/clientPanel.jsp"/>

</c:otherwise>
</c:choose>
</div>
</body>
</html>
