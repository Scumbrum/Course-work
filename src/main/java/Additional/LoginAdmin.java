package Additional;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LoginAdmin extends LoginClient {
    @Override
    public boolean chekCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("login");
        if(login==null || !login.equals(getServletContext().getInitParameter("admin"))) {
            return false;
        }
        return true;
    }
}
