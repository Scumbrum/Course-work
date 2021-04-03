package AdminManipulation;

import Database.UpdateChannel;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

@WebServlet(name = "ChannelController", value = "/ChannelController")
public class ChannelController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action =request.getParameter("action");
        String id = request.getParameter("id");
        String host = getServletContext().getInitParameter("database");
        String user = getServletContext().getInitParameter("user");
        String dbpassword = getServletContext().getInitParameter("password");
        String type = "com.mysql.cj.jdbc.Driver";
            try (UpdateChannel uc = new UpdateChannel(host, user, dbpassword, type)) {
                if(action!=null) {
                    switch (action) {
                        case ("Delete"):
                            uc.delete(Integer.parseInt(id));
                            break;
                        case ("Add"):
                            String name = request.getParameter("newChannel");
                            uc.add(name);
                            break;
                    }
                }
                 TreeMap<String,Integer> list = uc.getChannels();
                request.setAttribute("chlist", list);
            } catch (SQLException | ClassNotFoundException throwables) {
                response.sendError(500);
            }
        request.setAttribute("controller", "channels");
        RequestDispatcher rd = request.getRequestDispatcher("adminpage.jsp");
        rd.forward(request,response);
    }
}
