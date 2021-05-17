package actions;



import Additional.LoginAdmin;
import Database.UpdateData;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "Signin", value = "/Signin")
public class Signin extends LoginAdmin {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("Signin");
        HttpSession session = request.getSession();
        if(session.getAttribute("login")!=null){
            RequestDispatcher rd = request.getRequestDispatcher("CheckRank");
            rd.forward(request,response);
            return;
        }
        if(action==null){
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request,response);
            return;
        }
        if(action.equals("Signin")){
            String name = request.getParameter("login");
            String password = request.getParameter("password");
            try {
                boolean exist = false;
                LOCK.readLock().lock();
                try {
                    exist = checkCustomer(name, password);
                }finally {
                    LOCK.readLock().unlock();
                }
                if(exist){
                    session.setAttribute("login", name);
                    RequestDispatcher rd = request.getRequestDispatcher("CheckRank");
                    rd.forward(request,response);
                } else {
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
            RequestDispatcher rd = request.getRequestDispatcher("DoRegister");
            rd.forward(request,response);
        }
    }
    private boolean checkCustomer(String name,String password) throws ClassNotFoundException, SQLException, IOException {
        ArrayList<String> list = getparam();
        try (
                UpdateData uc = new UpdateData(list.get(0),list.get(1),list.get(2),list.get(3))) {
            ArrayList<ArrayList<String>> select = uc.select("select * from custumer2");
            for (ArrayList<String> customer:select){
                if(name.equals(customer.get(0)) && password.equals(customer.get(1))){
                    return true;
                }
            }
            return false;
        }
    }
}
