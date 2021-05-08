package AdminManipulation;

import Additional.Login;
import Database.UpdateChannel;
import Exceptions.TheSameName;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@WebServlet(name = "ProgramController", value = "/ProgramController")
public class ProgramController extends Login {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ch=chekCustomer(request,response);
        if(!ch){
            return;
        }
        String action =request.getParameter("action");
        String id = request.getParameter("id");
        ArrayList<String> params = getparam();
        try (UpdateChannel uc = new UpdateChannel(params.get(0), params.get(1), params.get(2), params.get(3)))  {
            if(action!=null) {
                switch (action) {
                    case ("Refactor"):
                        LOCK.readLock().lock();
                        try {
                            ArrayList<ArrayList<String>> list = showParam(request, uc, id);
                            ArrayList<String> meta = uc.getMeta("program");
                            meta.remove(0);
                            request.setAttribute("meta", meta);
                            request.setAttribute("id", id);
                            request.setAttribute("selected", list.get(0));
                        }finally {
                            LOCK.readLock().unlock();
                        }
                        break;
                    case ("doRefactor"):
                        LOCK.writeLock().lock();
                        try {
                            doRefactor(request,uc,id);
                        } finally {
                            LOCK.writeLock().unlock();
                        }
                        break;
                }
            }
        } catch (ClassNotFoundException throwables) {
            response.sendError(500);
        } catch (SQLException e){
            request.setAttribute("exception","Invalid data");
        } catch (TheSameName e){
            request.setAttribute("exception","existTransfer");
        }
        try (UpdateChannel uc = new UpdateChannel(params.get(0), params.get(1), params.get(2), params.get(3))) {
            LOCK.readLock().lock();
            try{
                ArrayList<ArrayList<String>> list = makeList(uc);
                request.setAttribute("prlist", list);
            }finally {
                LOCK.readLock().unlock();
            }
        }catch (SQLException | ClassNotFoundException e){
            response.sendError(500);
        }
        request.setAttribute("controller", "program");
        RequestDispatcher rd = request.getRequestDispatcher("adminpage.jsp");
        rd.forward(request,response);
    }


    private void doRefactor(HttpServletRequest request, UpdateChannel uc,String id) throws SQLException, TheSameName {
            ArrayList<String> resultmeta = uc.getMeta("program");
            resultmeta.remove(0);
            String time = request.getParameter(resultmeta.get(0));
            String day = request.getParameter(resultmeta.get(1));
            String sql = "select id from program where time = '" + time + "' and day = '" + day + "'";
            ArrayList<ArrayList<String>> existed = uc.select(sql);
            if (existed.size() == 0) {
                uc.add("insert into program (`time`,`day`) VALUES ('" + time + "','" + day + "')");
                existed = uc.select(sql);
            }
            if (chekExistTransfer(request, uc, id)) {
                throw new TheSameName("TheSameTime");
            } else {
                uc.add("update transfer set program_id = '" + existed.get(0).get(0) + "' where id = '" + id + "'");
            }
        }


    private ArrayList<ArrayList<String>> showParam(HttpServletRequest request,UpdateChannel uc, String id) throws SQLException {
        request.setAttribute("action","Refactor");
            String sql = "select id,name,program_id from transfer where id ='" + id + "'";
            ArrayList<ArrayList<String>> list = uc.select(sql);
            sql = "select time, day from program where id ='" + list.get(0).get(2) + "'";
            ArrayList<String> temp = uc.select(sql).get(0);
            list.get(0).remove(2);
            list.get(0).addAll(temp);
            return list;
    }

   private boolean chekExistTransfer(HttpServletRequest request, UpdateChannel uc,String id) throws SQLException {
            ArrayList<ArrayList<String>> transfers = new ArrayList<>();
            ArrayList<String> meta = uc.getMeta("program");
            String chanel = uc.select("select chanel_id from transfer where id ='" + id + "'").get(0).get(0);
            meta.remove(0);
            String time = request.getParameter(meta.get(0));
            String day = request.getParameter(meta.get(1));
            String sql = "select id from program where time = '" + time + "' and day = '" + day + "'";
            String program = uc.select(sql).get(0).get(0);
            transfers = uc.select("select id from transfer where chanel_id ='" + chanel + "' and program_id ='" + program + "'");
            if (transfers.size() > 0) {
                return true;
            } else {
                return false;
            }
        }

    private ArrayList<ArrayList<String>> makeList(UpdateChannel uc) throws SQLException {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ArrayList<ArrayList<String>> transfers = uc.select("select id, chanel_id, name, program_id from transfer");
        for(int row=0;row<transfers.size();row++) {
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < transfers.get(row).size(); i++) {
                if (i==3) {
                    String pid = transfers.get(row).get(i);
                    String sql = "select time, day from program where id ='" + pid + "'";
                    temp.addAll(uc.select(sql).get(0));
                }
                if (i==0 | i==2) {
                    temp.add(transfers.get(row).get(i));
                }
            }
            list.add(temp);
        }
        return list;
    }
    }
