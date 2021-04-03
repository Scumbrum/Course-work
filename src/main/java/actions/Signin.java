package actions;



import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "Signin", value = "/Signin")
public class Signin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("Signin");
        HttpSession session = request.getSession();
        if(action.equals("Signin")){
            String name = request.getParameter("login");
            String password = request.getParameter("password");
            try {
                boolean exist =checkCustomer(name,password);
                if(exist){
                    session.setAttribute("login", name);
                    RequestDispatcher rd = request.getRequestDispatcher("CheckRank");
                    rd.forward(request,response);
                } else{
                    RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                    request.setAttribute("exception","invalidcustomer");
                    rd.forward(request,response);
                }
            } catch (ClassNotFoundException e) {
                response.sendError(500);
            } catch (SQLException throwables) {
                response.sendError(500);
            }
        }
        if(action.equals("Checkin")){
            response.sendRedirect("register.jsp");
        }
    }
    private boolean checkCustomer(String name,String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String host = getServletContext().getInitParameter("database");
        String user = getServletContext().getInitParameter("user");
        String dbpassword = getServletContext().getInitParameter("password");
        try (
            Connection c = DriverManager.getConnection(host,user,dbpassword)) {
            Statement st = c.createStatement();
            ResultSet select = st.executeQuery("select * from custumer2");
            while(select.next()){
                if(name.equals(select.getString(1)) && password.equals(select.getString(2))){
                    return true;
                }
            }
            return false;
        }
    }
}
