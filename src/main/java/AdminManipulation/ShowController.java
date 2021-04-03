package AdminManipulation;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ShowController", value = "/ShowController")
public class ShowController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String controller = request.getParameter("controller");
         switch (controller){
             case("refactChannels"):
                 response.sendRedirect("ChannelController");
                 break;
             case ("refactTransfers"):
                 response.sendRedirect("TransferController");
                 break;
             case ("refactProgram"):
                 response.sendRedirect("ProgramController");
                 break;
             default:
                 response.sendError(404);
         }
    }
}
