package Client;

import Database.UpdateChannel;
import Database.UpdateProgram;
import Database.UpdateTransfer;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

@WebServlet(name = "ClientView", value = "/ClientView")
public class ClientView extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String host = getServletContext().getInitParameter("database");
        String user = getServletContext().getInitParameter("user");
        String sort = request.getParameter("sortby");
        String dbpassword = getServletContext().getInitParameter("password");
        String type = "com.mysql.cj.jdbc.Driver";
        try (UpdateTransfer ut = new UpdateTransfer(host, user, dbpassword, type);
             UpdateChannel uc = new UpdateChannel(host, user, dbpassword, type);
             UpdateProgram up = new UpdateProgram(host, user, dbpassword, type)) {
            if (action != null) {
                switch (action) {
                    case ("View"):
                        ArrayList<String> paramlist = new ArrayList<>();
                        ArrayList<String> meta= new ArrayList<>();
                        ArrayList<ArrayList<String>> namelist = ut.getData("id=" + id, "chanel_id,name,description, program_id");
                        ArrayList<String> temp = ut.getMeta();
                        temp.remove(0);
                        temp.remove(3);
                        meta.addAll(temp);
                        temp = up.getMeta();
                        temp.remove(0);
                        meta.addAll(temp);
                        paramlist.add(uc.getName(namelist.get(0).get(0)));
                        paramlist.add(namelist.get(0).get(1));
                        paramlist.add(namelist.get(0).get(2));
                        ArrayList<ArrayList<String>> date = up.getDate("id=" + namelist.get(0).get(3), "time,day");
                        paramlist.add(date.get(0).get(0));
                        paramlist.add(date.get(0).get(1));

                        request.setAttribute("data", paramlist);
                        request.setAttribute("meta",meta);
                        request.setAttribute("action",action);
                }
            }
            TreeMap<String, String> showlist = new TreeMap<>();
            if(sort!=null){
                if(sort.equals("date")) {
                    ArrayList<ArrayList<String>> date = up.getData("", "id", "day, time");
                    if(date.size()==0){
                        response.sendError(505);
                        return;
                    }
                    for (ArrayList<String> i : date) {
                        ArrayList<ArrayList<String>> temp = ut.getData("program_id=" + i.get(0), "name,id");
                        if(temp.size()!=0) {
                            showlist.put(temp.get(0).get(0), temp.get(0).get(1));
                        }
                    }
                }
                if(sort.equals("channel")){
                    ArrayList<ArrayList<String>> name = uc.getData("", "id", "name");
                    if(name.size()==0){
                        response.sendError(505);
                        return;
                    }
                    for (ArrayList<String> i : name) {
                        ArrayList<ArrayList<String>> temp = ut.getData("chanel_id=" + i.get(0), "name,id");
                        if(temp.size()!=0) {
                            showlist.put(temp.get(0).get(0), temp.get(0).get(1));
                        }
                    }
                }
            }else{
                ArrayList<ArrayList<String>> namelist = ut.getData("", "name,id");
                for (ArrayList<String> name : namelist) {
                    showlist.put(name.get(0), name.get(1));
                }
            }
            request.setAttribute("showlist", showlist);
        } catch (SQLException | ClassNotFoundException throwables) {
            response.sendError(500);
        }
        RequestDispatcher rd = request.getRequestDispatcher("clientpage.jsp");
        rd.forward(request,response);
    }
}
