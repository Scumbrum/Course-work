<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 05.04.2021
  Time: 17:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<c:if test="${!empty requestScope.exception}">
    <c:set var="noName" value="noName"/>
    <c:set var="existReference" value="existReference"/>
    <c:set var="noNameTransfer" value="noNameTransfer"/>
    <c:set var="existTransfer" value="existTransfer"/>
    <c:set var="theSame" value="theSame"/>
    <c:set var="noChannel" value="noChannel"/>
    <c:set var="sameTransfer" value="sameTransfer"/>
    <c:set var="invalidData" value="Invalid data"/>
    <c:if test="${requestScope.exception==noName}">
        <script type="application/javascript">alert("Пуста назва каналу")</script>
    </c:if>
    <c:if test="${requestScope.exception==noNameTransfer}">
        <script type="application/javascript">alert("Пуста назва програми")</script>
    </c:if>
    <c:if test="${requestScope.exception==theSame}">
        <script type="application/javascript">alert("Існуючий канал")</script>
    </c:if>
    <c:if test="${requestScope.exception==existTransfer}">
        <script type="application/javascript">alert("Цей час зайнятий")</script>
    </c:if>
    <c:if test="${requestScope.exception==noChannel}">
        <script type="application/javascript">alert("Неіснуючий канал")</script>
    </c:if>
    <c:if test="${requestScope.exception==sameTransfer}">
        <script type="application/javascript">alert("Цей час зайнятий")</script>
    </c:if>
    <c:if test="${requestScope.exception==existReference}">
        <script type="application/javascript">alert("На цьому каналі забагато передач для видалення")</script>
    </c:if>
    <c:if test="${requestScope.exception==invalidData}">
        <script type="application/javascript">alert("Неправильні дані")</script>
    </c:if>
</c:if>
</body>
</html>
