package AdminManipulation;

import Database.UpdateChannel;
import Database.UpdateTransfer;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

@WebServlet(name = "TransferController", value = "/TransferController")
public class TransferController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action =request.getParameter("action");
        String id = request.getParameter("id");
        String host = getServletContext().getInitParameter("database");
        String user = getServletContext().getInitParameter("user");
        String dbpassword = getServletContext().getInitParameter("password");
        String type = "com.mysql.cj.jdbc.Driver";
        try (UpdateTransfer ut = new UpdateTransfer(host, user, dbpassword, type)) {
            if(action!=null) {
                switch (action) {
                    case ("delete"):
                        ut.delete(Integer.parseInt(id));
                        break;
                    case ("Add"):
                        ArrayList<String> data= ut.getNewData();
                        request.setAttribute("selected",data);
                        request.setAttribute("action","doAdd");
                        request.setAttribute("meta",ut.getMeta());
                        break;
                    case ("refactor"):
                        ArrayList<String> dataRef= ut.getData("id="+id,"*").get(0);
                        try(UpdateChannel uc = new UpdateChannel(host, user, dbpassword, type)){
                            dataRef.set(1,uc.getName(dataRef.get(1)));
                        }
                        request.setAttribute("selected",dataRef);
                        request.setAttribute("id",id);
                        request.setAttribute("action","doRefactor");
                        request.setAttribute("meta",ut.getMeta());
                        break;
                    case("doAdd"):
                        ArrayList<String> params = new ArrayList<>();
                        ArrayList<String> meta = ut.getMeta();
                        for(int i=1;i<meta.size()-1;i++){
                            String param = request.getParameter(meta.get(i));
                            if(meta.get(i).equals("chanel_id")){
                                try(UpdateChannel uc = new UpdateChannel(host, user, dbpassword, type)) {
                                    params.add(uc.getId(param));
                                }
                            }else{
                                params.add(param);
                            }
                        }
                        ut.add(params);
                        break;
                    case("doRefactor"):
                        ArrayList<String> metaRef = ut.getMeta();
                        ArrayList<String> curr = ut.getData("id="+id, "*").get(0);
                        for(int i=1;i<metaRef.size()-1;i++){
                            String param = request.getParameter(metaRef.get(i));
                            if(metaRef.get(i).equals("chanel_id")){
                                try(UpdateChannel uc = new UpdateChannel(host, user, dbpassword, type)) {
                                    if(!curr.get(i).equals(uc.getId(param))) {
                                        ut.alter(metaRef.get(i), uc.getId(param), id);
                                    }
                                }
                            } else {
                                if(!curr.get(i).equals(param)) {
                                    ut.alter(metaRef.get(i), param, id);
                                }
                            }
                        }
                        break;
                }
            }
            ArrayList<ArrayList<String>> list = ut.getData("","id,name");
            request.setAttribute("trlist", list);
        } catch (SQLException | ClassNotFoundException throwables) {
            response.sendError(500);
        }
        request.setAttribute("controller", "transfers");
        RequestDispatcher rd = request.getRequestDispatcher("adminpage.jsp");
        rd.forward(request,response);
    }
}
