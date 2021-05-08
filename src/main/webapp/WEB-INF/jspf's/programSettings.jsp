<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 05.04.2021
  Time: 17:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div class="refactorPanel">
    <div>
<table class="controlTable">
    <c:set var="programList" scope="page" value="${requestScope.prlist}"/>
    <c:forEach var="eachProgram" items="${programList}">
        <tr class="controlLine">

            <td>
                <h1 class="param"><c:out value="${eachProgram.get(1)}"/></h1>
            </td>
            <td>
                <h1 class="param"><c:out value="${eachProgram.get(2)}"/></h1>
            </td>
            <td>
                <h1 class="param"><c:out value="${eachProgram.get(3)}"/></h1>
            </td>
            <td>
                <form class="refactor" action="Signin" method="post">
                    <input type="hidden" name="controller" value="refactProgram">
                    <input type="hidden" name="id" value="${eachProgram.get(0)}">
                    <input class="quit" type="submit" name ="action" value="Refactor">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
    </div>
<c:if test="${!empty requestScope.action}">
    <c:set value="${requestScope.selected}" var="selected"/>
    <c:set value="${requestScope.meta}" var="meta"/>
    <div class="settings">
    <h1 class="capitalName"><c:out value="${selected.get(1)}"/></h1>
    <form class="refactor" action="Signin" method="post">
        <input type="hidden" name="controller" value="refactProgram">
        <table class="controlTable">
            <tr class="controlLine">
                <c:forEach var="change" items="${selected}" varStatus="count">
                    <c:if test="${count.count>2}">
                        <td>
                            <p>
                                <h1 class="param"><c:out value="${meta.get(count.count-3)}"/> :</h1>
                                <input type="text" name ="${meta.get(count.count-3)}" value="${selected.get(count.count-1)}"/>
                            </p>
                        </td>
                    </c:if>
                </c:forEach>
            </tr>
        </table>
        <input type="hidden" name ="id" value="<%=request.getAttribute("id")%>">
        <input type="hidden" name="action" value="doRefactor"/>
        <input class="button" type="submit" value="Refactor"/>
    </form>
</c:if>
    </div>
</div>
</body>
</html>
