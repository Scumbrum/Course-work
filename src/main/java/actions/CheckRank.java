package actions;

import Additional.LoginClient;
import Database.UpdateData;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "CheckRank", value = "/CheckRank")
public class CheckRank extends LoginClient {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("login");
        if(!chekCustomer(request,response)){
            response.sendRedirect("index.jsp");
            return;
        }
        ArrayList<String> list = getparam();
        try (
                UpdateData uc = new UpdateData(list.get(0),list.get(1),list.get(2),list.get(3))) {
            String query = "select * from hierarchy where custumerlogin=" + "'"+name+"'";
            ArrayList<ArrayList<String>> select = uc.select(query);
            String rank = select.get(0).get(1);
            if (rank.equals("admin")){
                RequestDispatcher rd = request.getRequestDispatcher("ShowController");
                rd.forward(request,response);
            }
            else {
                RequestDispatcher rd = request.getRequestDispatcher("ClientView");
                rd.forward(request,response);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            response.sendError(500);
        }
    }
}
