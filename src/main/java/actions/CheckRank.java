package actions;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "CheckRank", value = "/CheckRank")
public class CheckRank extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("login");
        String host = getServletContext().getInitParameter("database");
        String user = getServletContext().getInitParameter("user");
        String dbpassword = getServletContext().getInitParameter("password");
        try (
                Connection c = DriverManager.getConnection(host, user, dbpassword)) {
            Statement st = c.createStatement();
            String query = "select * from hierarchy where custumerlogin=" + "'"+name+"'";
            ResultSet select = st.executeQuery(query);
            select.next();
            String rank = select.getString(2);
            if (rank.equals("admin")){
                response.sendRedirect("adminpage.jsp");
            }
            else {
                response.sendRedirect("clientpage.jsp");
            }
        } catch (SQLException throwables) {
            response.sendError(500);
        }
    }
}
