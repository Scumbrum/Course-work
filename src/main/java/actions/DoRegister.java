package actions;


import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "DoRegister", value = "/DoRegister")
public class DoRegister extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("login");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
        if (!password1.equals(password2) || password1.equals("")) {
            request.setAttribute("exception", "notsame");
            rd.forward(request, response);
        } else {
            try {
                if(registrate(name, password1)){
                    response.sendRedirect("Successful.html");
                }
                else{
                    request.setAttribute("exception", "existlogin");
                    rd.forward(request, response);
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                response.sendError(500);
            }
        }
    }

    public boolean registrate(String login, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String host = getServletContext().getInitParameter("database");
        String user = getServletContext().getInitParameter("user");
        String dbpassword = getServletContext().getInitParameter("password");
        try (
                Connection c = DriverManager.getConnection(host, user, dbpassword)) {
            Statement st = c.createStatement();
            ResultSet select = st.executeQuery("select * from custumer2");

            while (select.next()){
                if(select.getString(1).equals(login)){
                    return false;
                }
            }
            String query = "insert into custumer2 (`login`, `password`) VALUES ('" + login+"',"+"'"+password+"'"+")";
            st.execute(query);
            query = "insert into hierarchy (`custumerlogin`) VALUE ('"+login+"')";
            st.execute(query);
            return true;
        }
    }
}
