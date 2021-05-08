<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 05.04.2021
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div class="refactorPanel">
    <div class="settings">
<table class="controlTable">
    <c:set var="trlist" scope="page" value="${requestScope.trlist}"/>
    <c:forEach var="transfer" items="${trlist}">
        <tr class="controlLine">
            <td>
                <h1 class="param"><c:out value="${transfer.get(1)}"/></h1>
            </td>
            <td class="radioCell">
                <form action="TransferController" method="post">
                    <input type="hidden" name="id" value="${transfer.get(0)}"/>
                    <p class="radioP"><input type="radio" name="action" value="delete"/>Delete</p>
                    <p class="radioP"><input type="radio" name="action" value="refactor"/>Refactor</p>
                    <br>
                    <input class="quit" type="submit" value="Apply">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<form action="TransferController" method="post">
    <input class="button" type="submit" name="action" value="Add">
</form>
</div>
<c:if test="${!empty requestScope.selected}">
    <div class="settings">
    <c:set var="params" value="${requestScope.selected}"/>
    <c:set var="metas" value="${requestScope.meta}"/>
    <form action="Signin" method="post">
        <input type="hidden" name="controller" value="refactTransfers">
        <table class="controlTable">
            <tr class="controlLine">
                <c:forEach var="meta" items="${metas}" varStatus="count">
                    <c:if test="${count.count>1 && count.count<5}">
                        <td>
                            <h1 class="param"><c:out value="${meta}"/></h1>
                        </td>
                    </c:if>
                </c:forEach>
            </tr>
            <tr>
                <c:forEach var="param" items="${params}" varStatus="count">
                    <c:if test="${count.count>1 && count.count<5}">
                        <td>
                            <input class="whiteInput" type="text" name="${metas.get(count.count-1)}" value="${params.get(count.count-1)}"/>
                        </td>
                    </c:if>
                </c:forEach>
            </tr>
        </table>
        <c:set var="doRefactor" scope="page" value="doRefactor"/>
        <c:set var="doAdd" scope="page" value="doAdd"/>
        <c:choose>
            <c:when test="${requestScope.action==doRefactor}">
                <input type="hidden" name ="id" value="<%=request.getAttribute("id")%>">
                <input type="hidden" name="action" value="doRefactor">
                <input class="button" type="submit" value="Refactor"/>
            </c:when>
            <c:when test="${requestScope.action==doAdd}">
                <input type="hidden" name="action" value="doAdd">
                <input class="button" type="submit" value="Add"/>
            </c:when>
        </c:choose>
    </form>
    </div>
</c:if>
</div>
</body>
</html>
