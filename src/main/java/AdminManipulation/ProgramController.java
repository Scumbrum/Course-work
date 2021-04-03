package AdminManipulation;

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

@WebServlet(name = "ProgramController", value = "/ProgramController")
public class ProgramController extends HttpServlet {
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
        try (UpdateTransfer uc = new UpdateTransfer(host, user, dbpassword, type);
        UpdateProgram up = new UpdateProgram(host, user, dbpassword, type)) {
            if(action!=null) {
                switch (action) {
                    case ("Refactor"):
                        request.setAttribute("action","Refactor");
                        ArrayList<ArrayList<String>> list = uc.getData("id='"+id+"'","id,name,program_id");
                        System.out.println(list);
                        ArrayList<String> temp = up.getDate("id="+list.get(0).get(2),"time,day").get(0);
                        list.get(0).remove(2);
                        list.get(0).addAll(temp);
                        System.out.println(list);
                        ArrayList<String> meta = up.getMeta();
                        meta.remove(0);
                        request.setAttribute("meta",meta);
                        request.setAttribute("id",id);
                        request.setAttribute("selected",list.get(0));
                        break;
                    case ("doRefactor"):
                        ArrayList<String> resultmeta = up.getMeta();
                        resultmeta.remove(0);
                        String time = request.getParameter(resultmeta.get(0));
                        String day = request.getParameter(resultmeta.get(1));
                        ArrayList<ArrayList<String>> existed= up.getDate("time='"+time+"' AND day='"+day+"'","id");
                        System.out.println(existed);
                        if(existed.size()==0){
                            up.add(time,day);
                            existed = up.getDate("time='"+time+"' AND day='"+day+"'","id");
                            System.out.println(existed);
                        }
                        uc.alter("program_id", existed.get(0).get(0),id);
                }
            }
            ArrayList<ArrayList<String>> list = new ArrayList<>();
            ArrayList<ArrayList<String>> transfers = uc.getData("","id,name,program_id");
            for(int row=0;row<transfers.size();row++) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 0; i < transfers.get(row).size(); i++) {
                    if (i==2) {
                        String pid = transfers.get(row).get(i);
                        temp.addAll(up.getDate("id='" +pid +"'","time,day").get(0));
                    }
                    if (i==0) {
                        temp.add(0, transfers.get(row).get(i));
                    }
                    if (i==1) {
                        temp.add(1, transfers.get(row).get(i));
                    }
                }
                list.add(temp);
            }
            System.out.println(list);
            request.setAttribute("prlist", list);
        } catch (SQLException | ClassNotFoundException throwables) {
            response.sendError(500);
        }
        request.setAttribute("controller", "program");
        RequestDispatcher rd = request.getRequestDispatcher("adminpage.jsp");
        rd.forward(request,response);
    }
    }
