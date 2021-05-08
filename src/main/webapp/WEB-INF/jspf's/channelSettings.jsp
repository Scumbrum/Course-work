<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 05.04.2021
  Time: 17:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div class="refactorPanel">
    <div class="settings">
<table class="controlTable">
    <c:set var="chlist" scope="page" value="${requestScope.chlist}"/>
    <c:forEach var="channel" items="${chlist}">
        <tr class="controlLine">
            <td>
                <h1 class="param"><c:out value="${channel.get(1)}"/></h1>
            </td>
            <td>
                <form class="refactor" action="Signin" method="post">
                    <input type="hidden" name="controller" value="refactChannels">
                    <input type="hidden" name="id" value="${channel.get(0)}">
                    <input class="quit" type="submit" name="action" value="Delete"/>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<form action="Signin" method="post">
    <input type="hidden" name="controller" value="refactChannels">
    <input class="whiteInput" type="text" name="newChannel"/>
    <input class="button" type="submit" name="action" value="Add"/>
</form>
    </div>
</div>
</body>
</html>
