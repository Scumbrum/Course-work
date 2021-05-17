package AdminManipulation;

import Additional.LoginAdmin;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ShowController", value = "/ShowController")
public class ShowController extends LoginAdmin {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!chekCustomer(request,response)){
            response.sendRedirect("index.jsp");
            return;
        }
        String controller = request.getParameter("controller");
        String action = request.getParameter("action");
        if(action!=null && action.equals("Quit")){
            HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect("Signin");
            return;
        }
        if(controller==null){
            RequestDispatcher rd = request.getRequestDispatcher("adminpage.jsp");
            rd.forward(request,response);
            return;
        }
        RequestDispatcher rd;
         switch (controller){
             case("refactChannels"):
                 rd = request.getRequestDispatcher("ChannelController");
                 rd.forward(request,response);
                 break;
             case ("refactTransfers"):
                 rd = request.getRequestDispatcher("TransferController");
                 rd.forward(request,response);
                 break;
             case ("refactProgram"):
                 rd = request.getRequestDispatcher("ProgramController");
                 rd.forward(request,response);
                 break;
             default:
                 response.sendError(404);
         }
    }
}
