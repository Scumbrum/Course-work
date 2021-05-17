package Additional;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LoginClient extends HttpServlet {
    protected static final ReentrantReadWriteLock LOCK= new ReentrantReadWriteLock(true);
    public ArrayList<String> getparam(){
        ArrayList<String> params = new ArrayList<>();
        params.add(getServletContext().getInitParameter("database"));
        params.add(getServletContext().getInitParameter("user"));
        params.add(getServletContext().getInitParameter("password"));
        params.add("com.mysql.cj.jdbc.Driver");
        return params;
    }
    public boolean chekCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException{
    HttpSession session = request.getSession();
    String login = (String) session.getAttribute("login");
        if(login==null) {
            return false;
        }
        return true;
    }
}
