package Additional;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
public abstract class LoginClient extends Login {
    @Override
    public boolean chekCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException{
    HttpSession session = request.getSession();
    String login = (String) session.getAttribute("login");
        if(login==null) {
            response.sendRedirect("index.jsp");
            return false;
        }
        return true;
    }
}
