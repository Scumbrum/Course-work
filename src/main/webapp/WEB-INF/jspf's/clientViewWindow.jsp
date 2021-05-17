<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<c:set var="View" value="View"/>
<c:set value="${requestScope.action}" var="action"/>
<c:set value="${requestScope.id}" var="id"/>
<c:if test="${action==View}">
    <c:set value="${requestScope.data}" var="data"/>
    <c:set value="${requestScope.meta}" var="meta"/>
    <table class="controlTable">
    <tr class="controlLine">
    <c:forEach var="param" items="${meta}">
        <td>
            <h1 class="param"><c:out value="${pageScope.param}"/></h1>
        </td>
    </c:forEach>
    </tr>
    <tr class="controlLine">
    <c:forEach var="param" items="${data}">
        <td>
            <h1 class="param">${pageScope.param}</h1>
        </td>
    </c:forEach>
    </tr>
    </table>
</c:if>
</body>
</html>
