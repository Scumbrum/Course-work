package actions;


import Additional.LoginClient;
import Database.UpdateData;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

@WebServlet(name = "DoRegister", value = "/DoRegister")
public class DoRegister extends LoginClient {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("login");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        RequestDispatcher rd = request.getRequestDispatcher("register.jsp");
        if(name==null){
            rd.forward(request,response);
            return;
        }
        name=name.toLowerCase();
        if(name.equals("")){
            request.setAttribute("exception","empty");
            rd.forward(request,response);
            return;
        }
        if (!password1.equals(password2) || password1.equals("")) {
            request.setAttribute("exception", "notsame");
            rd.forward(request, response);
        } else {
            LOCK.writeLock().lock();
            try {
                if(registrate(name, password1)){
                    RequestDispatcher success = request.getRequestDispatcher("WEB-INF/Successful.html");
                    success.forward(request,response);
                }
                else{
                    request.setAttribute("exception", "existlogin");
                    rd.forward(request, response);
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                response.sendError(500);
            }finally {
                LOCK.writeLock().unlock();
            }
        }
    }

    public boolean registrate(String login, String password) throws SQLException, ClassNotFoundException, IOException {
        ArrayList<String> list = getparam();
        try (
                UpdateData uc = new UpdateData(list.get(0),list.get(1),list.get(2),list.get(3))) {
            ArrayList<ArrayList<String>> select = uc.select("select * from custumer2");

            for(ArrayList<String> logins:select){
                if (logins.get(0).equals(login)){
                    return false;
                }
            }
                String query = "insert into custumer2 (`login`, `password`) VALUES ('" + login+"',"+"'"+password+"'"+")";
                uc.add(query);
                query = "insert into hierarchy (`custumerlogin`) VALUE ('"+login+"')";
                uc.add(query);
            }
            return true;
        }
}
