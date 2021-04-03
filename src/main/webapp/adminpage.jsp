<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.TreeMap" %><%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 29.03.2021
  Time: 13:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AdminPage</title>
</head>
<body>
<header>
    <p align="middle">Hello,<%=session.getAttribute("login")%></p>
</header>
<%
    if(
            session.getAttribute("login")==null
    ){
        response.sendRedirect("index.jsp");
    }
%>
<table border="2" cellpadding="5">
    <tr>
        <td>
            <h3>Channels</h3>
        </td>
        <td>
            <form action="ShowController" method="post">
                <input type="hidden" name ="controller" value="refactChannels"/>
                <input type="submit" value="Refactor">
            </form>
        </td>
    </tr>
    <tr>
        <td>
            <h3>Transfers</h3>
        </td>
        <td>
            <form action="ShowController" method="post">
                <input type="hidden" name ="controller" value="refactTransfers"/>
                <input type="submit" value="Refactor">
            </form>
        </td>
    </tr>
    <tr>
        <td>
            <h3>Program</h3>
        </td>
        <td>
            <form action="ShowController" method="post">
                <input type="hidden" name ="controller" value="refactProgram"/>
                <input type="submit" value="Refactor">
            </form>
        </td>
    </tr>
</table>
<% if(request.getAttribute("controller")!=null){%>
<%
    if(request.getAttribute("controller").equals("channels")){
%>
<table border="2">
    <%
        TreeMap<String,Integer> chlist= (TreeMap<String, Integer>) request.getAttribute("chlist");
        int count=1;
        Object[] keys = chlist.keySet().toArray();
        for(Object i: keys){
    %>
    <tr>
        <td>
            <%=(String) i%>
        </td>
        <td>
            <form action="ChannelController" method="post">
                <input type="hidden" name="id" value="<%=chlist.get(i)%>">
                <input type="submit" name="action" value="Delete"/>
            </form>
        </td>
    </tr>
    <%
    }
    %>
</table>
<form action="ChannelController" method="post">
    <input type="text" name="newChannel"/>
    <input type="submit" name="action" value="Add"/>
</form>
<%
    }
%>
<%
    if(request.getAttribute("controller").equals("transfers")){
%>
<table border="2">
<%
    ArrayList<ArrayList<String>>  trlist= (ArrayList<ArrayList<String>>) request.getAttribute("trlist");
    for(ArrayList<String> i: trlist){
%>
    <tr>
        <td>
            <%=i.get(1)%>
        </td>
        <td>
            <form action="TransferController" method="post">
                <input type="hidden" name="id" value="<%=i.get(0)%>"/>
                <p><input type="radio" name="action" value="delete"/>Delete</p>
                <p><input type="radio" name="action" value="refactor"/>Refactor</p>
                <br>
                <input type="submit" value="Apply">
            </form>
        </td>
    <%}
%>
    </tr>
</table>
<form action="TransferController" method="post">
    <input type="submit" name="action" value="Add">
</form>
<% if(request.getAttribute("selected")!=null){
    ArrayList<String> param = (ArrayList<String>) request.getAttribute("selected");
    ArrayList<String> meta = (ArrayList<String>) request.getAttribute("meta");
%>
<form action="TransferController" method="post">
<table>
    <tr>
        <%
            for (int i=1; i<param.size()-1;i++){
        %>
        <td>
            <%=meta.get(i)%>
        </td>
        <%
            }
        %>
    </tr>
    <tr>
    <%
        for (int i=1; i<param.size()-1;i++){
    %>
    <td>
        <input type="text" name="<%=meta.get(i)%>" value="<%=param.get(i)%>"/>
    </td>
<%
    }%>
</tr>
</table>
    <%
        if(request.getAttribute("action").equals("doRefactor")){
    %>
    <input type="hidden" name ="id" value="<%=request.getAttribute("id")%>">
    <input type="hidden" name="action" value="doRefactor">
    <input type="submit" value="Refactor"/>
    <%
        } else {%>
    <input type="hidden" name="action" value="doAdd">
    <input type="submit" value="Add"/>
    <%
        }
    %>
</form>
<%
    }
    }
%>
<%
    if(request.getAttribute("controller").equals("program")){
%>
<table>
    <%
        ArrayList<ArrayList<String>> programlist= (ArrayList<ArrayList<String>>) request.getAttribute("prlist");
        for(ArrayList<String> minilist: programlist){
    %>
    <tr>
        <%
            for(int i=1;i< minilist.size();i++){
        %>
        <td>
            <%=minilist.get(i)%>
        </td>
        <%
            }
        %>
        <td>
            <form action="ProgramController" method="post">
                <input type="hidden" name="id" value="<%=minilist.get(0)%>">
                <input type="submit" name ="action" value="Refactor">
            </form>
        </td>
    </tr>
    <%
        }
    %>
</table>
    <%
        if(request.getAttribute("action")!=null){
            ArrayList<String> selected = (ArrayList<String>) request.getAttribute("selected");
            ArrayList<String> meta = (ArrayList<String>) request.getAttribute("meta");
    %>
    <h1><%=selected.get(1)%></h1>
<form action="ProgramController" method="post">
    <table>
        <tr>
    <%
        for(int i=2;i<selected.size();i++){
    %>
            <td>
                <p> <%=meta.get(i-2)%> : <input type="text" name ="<%=meta.get(i-2)%>" value="<%=selected.get(i)%>"/></p>
            </td>
            <%
                }
            %>
        </tr>
    </table>
    <input type="hidden" name ="id" value="<%=request.getAttribute("id")%>">
    <input type="hidden" name="action" value="doRefactor"/>
    <input type="submit" value="Refactor"/>
</form>
<%
    }
%>
<%
    }
}
%>
</body>
</html>
