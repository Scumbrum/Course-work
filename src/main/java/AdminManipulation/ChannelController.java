package AdminManipulation;

import Additional.LoginAdmin;
import Database.UpdateData;
import Exceptions.NoNameChannel;
import Exceptions.TheSameName;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "ChannelController", value = "/ChannelController")
public class ChannelController extends LoginAdmin {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!chekCustomer(request,response)){
            response.sendRedirect("index.jsp");
            return;
        }
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        ArrayList<String> params = getparam();
        try (UpdateData uc = new UpdateData(params.get(0), params.get(1), params.get(2), params.get(3))) {
            if (action != null) {
                switch (action) {
                    case ("Delete"):
                            LOCK.writeLock().lock();
                            try {
                                if (chekExistTransfer(uc, id)) {
                                    throw new RuntimeException("Exist Reference");
                                }
                                uc.delete("delete from chanels where id ='" + id + "'");
                            }finally {
                                LOCK.writeLock().unlock();
                            }
                            break;
                    case ("Add"):
                        String name = request.getParameter("newChannel");
                        LOCK.writeLock().lock();
                        try{
                            if (name.equals("")) {
                                throw new NoNameChannel("noName");
                            }
                            ArrayList<ArrayList<String>> exist = uc.select("select * from chanels where name ='" + name + "'");
                            if (exist.size() != 0) {
                                throw new TheSameName("sameName");
                            } else {
                                uc.add("insert into chanels (`name`) VALUE ('" + name + "')");
                            }
                            break;
                        }
                        finally {
                            LOCK.writeLock().unlock();
                        }
                }
            }
        } catch (TheSameName e){
            request.setAttribute("exception", "theSame");
        }catch (NoNameChannel e){
            request.setAttribute("exception", "noName");
        }catch (RuntimeException e){
            request.setAttribute("exception", "existReference");
        }
        catch (ClassNotFoundException | SQLException throwables) {
            response.sendError(500);
        }
        try (UpdateData uc = new UpdateData(params.get(0), params.get(1), params.get(2), params.get(3))) {
            LOCK.readLock().lock();
                        try {
                            ArrayList<ArrayList<String>> list = uc.select("select * from chanels");
                            request.setAttribute("chlist", list);
                            request.setAttribute("controller", "channels");
                            RequestDispatcher rd = request.getRequestDispatcher("adminpage.jsp");
                            rd.forward(request, response);
                        }
                        finally {
                            LOCK.readLock().unlock();
                        }
        }   catch (SQLException | ClassNotFoundException e){
            response.sendError(500);
        }
    }

    private boolean chekExistTransfer(UpdateData uc, String id) throws SQLException {
        ArrayList<ArrayList<String>> exist= uc.select("select id from transfer where chanel_id ='" + id + "'");
        if(exist.size()!=0){
            return true;
        } return false;
    }
}
