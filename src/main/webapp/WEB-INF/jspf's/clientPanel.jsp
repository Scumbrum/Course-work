<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 05.04.2021
  Time: 17:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div class="clientPanel">
<form class="centralForm" action="ClientView" method="post">
    <h3 class="capitalName">Сортувати</h3>
    <p class="radioP"><input type="radio" name="sortby" value="date"/> За часом </p>
    <p class="radioP"><input type="radio" name="sortby" value="channel"/> По каналам </p>
    <input class="quit" type="submit" value="Sort"/>
</form>
</div>
<c:set var="showSet" value="${requestScope.showlist}"/>
<c:set var="showList" value="${showSet.keySet()}"/>
<div class="refactorPanel">
<table class="controlTable">
    <c:forEach var="show" items="${showList}">
    <tr class="controlLine">
        <c:forEach var="parameter" items="${showSet.get(show)}">
        <td>
            <h1 class="param"> <c:out value="${parameter}"/></h1>
        </td>
        </c:forEach>
        <td>
            <form class="refactor" action="Signin" method="post">
                <c:if test="${!empty requestScope.sortby}">
                    <input type="hidden" name="sortby" value="${requestScope.sortby}"/>
                </c:if>
                <input type="hidden" name="id" value="${show}"/>
                <input class="quit" type="submit" name="action" value="View"/>
            </form>
        </td>
    </tr>
    </c:forEach>
</table>
    <c:if test="${!empty requestScope.action}">

        <jsp:include page="clientViewWondow.jsp"/>

    </c:if>
</div>
</body>
</html>
