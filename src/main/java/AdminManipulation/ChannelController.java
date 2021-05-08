package AdminManipulation;

import Additional.Login;
import Database.UpdateChannel;
import Exceptions.NoChannel;
import Exceptions.NoNameChannel;
import Exceptions.TheSameName;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

@WebServlet(name = "ChannelController", value = "/ChannelController")
public class ChannelController extends Login {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ch=chekCustomer(request,response);
        if(!ch){
            return;
        }
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        ArrayList<String> params = getparam();
        try (UpdateChannel uc = new UpdateChannel(params.get(0), params.get(1), params.get(2), params.get(3))) {
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
                            ArrayList<ArrayList<String>> exist = uc.select("select * from chanels where name ='" + name + "'");
                            if (exist.size() != 0) {
                                throw new TheSameName("sameName");
                            }
                            if (name.equals("")) {
                                throw new NoNameChannel("noName");
                            } else {
                                uc.add("insert into chanels (`name`) VALUE ('" + name + "')");
                            }
                            break;
                        }catch (TheSameName e){
                            request.setAttribute("exception", "theSame");
                        }catch (NoNameChannel e){
                            request.setAttribute("exception", "noName");
                        }catch (RuntimeException e){
                            request.setAttribute("exception", "existReference");
                        }
                        finally {
                            LOCK.writeLock().unlock();
                        }
                }
            }
        } catch (ClassNotFoundException | SQLException throwables) {
            response.sendError(500);
        }
        try (UpdateChannel uc = new UpdateChannel(params.get(0), params.get(1), params.get(2), params.get(3))) {
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

    private boolean chekExistTransfer(UpdateChannel uc, String id) throws SQLException {
        ArrayList<ArrayList<String>> exist= uc.select("select id from transfer where chanel_id ='" + id + "'");
        if(exist.size()!=0){
            return true;
        } return false;
    }
}
