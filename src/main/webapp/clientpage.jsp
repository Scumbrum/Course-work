<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.TreeSet" %>
<%@ page import="java.util.TreeMap" %><%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 29.03.2021
  Time: 13:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    if(
            session.getAttribute("login")==null
    ){
        response.sendRedirect("index.jsp");
    }
%>
<p align="center">Hello client!</p>
<% if (request.getAttribute("showlist")==null){
    response.sendRedirect("ClientView");
} else{%>
<form action="ClientView" method="post">
    <h3>Сортувати</h3>
    <p><input type="radio" name="sortby" value="date"/> Свіжі</p>
    <p><input type="radio" name="sortby" value="channel"/> По каналам</p>
    <input type="submit" value="Sort"/>
</form>
<%
    TreeMap<String, String> showset = (TreeMap<String, String>) request.getAttribute("showlist");
    Object[] showlist = showset.descendingKeySet().toArray();
%>
<table border="2">
    <%
        for(Object show: showlist){
    %>
    <tr>
        <td>
            <%= (String)show%>
        </td>
        <td>
            <form action="ClientView" method="post">
            <input type="hidden" name="id" value="<%=showset.get(show)%>"/>
            <input type="submit" name="action" value="View"/>
            </form>
        </td>
    </tr>
</table>
<%
    }
        if(request.getAttribute("action")!=null){
            String action = (String) request.getAttribute("action");
            String id = (String) request.getAttribute("id");
            if(action.equals("View")){
                ArrayList<String> data = (ArrayList<String>) request.getAttribute("data");
                ArrayList<String> meta = (ArrayList<String>) request.getAttribute("meta");
%>
<table border="2">
    <tr>
        <%
            for(String param:meta){
        %>
        <td>
            <%=param%>
        </td>
        <%
            }
        %>
    </tr>
    <tr>
        <%
            for(String param:data){
        %>
        <td>
            <%=param%>
        </td>
        <%
            }
        %>
    </tr>
</table>

<%
            }
        }
    }
%>

</body>
</html>
