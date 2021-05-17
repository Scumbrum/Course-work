package AdminManipulation;

import Additional.LoginAdmin;
import Database.UpdateData;
import Exceptions.TheSameName;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "ProgramController", value = "/ProgramController")
public class ProgramController extends LoginAdmin {
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
        String action =request.getParameter("action");
        String id = request.getParameter("id");
        ArrayList<String> params = getparam();
        try (UpdateData uc = new UpdateData(params.get(0), params.get(1), params.get(2), params.get(3)))  {
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
            return;
        } catch (SQLException e){
            request.setAttribute("exception","Invalid data");
        } catch (TheSameName e){
            request.setAttribute("exception","existTransfer");
        }
        try (UpdateData uc = new UpdateData(params.get(0), params.get(1), params.get(2), params.get(3))) {
            LOCK.readLock().lock();
            try{
                ArrayList<ArrayList<String>> list = makeList(uc);
                request.setAttribute("prlist", list);
            }finally {
                LOCK.readLock().unlock();
            }
        }catch (SQLException | ClassNotFoundException e){
            response.sendError(500);
            return;
        }
        request.setAttribute("controller", "program");
        RequestDispatcher rd = request.getRequestDispatcher("adminpage.jsp");
        rd.forward(request,response);
    }


    private void doRefactor(HttpServletRequest request, UpdateData uc, String id) throws SQLException, TheSameName {
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


    private ArrayList<ArrayList<String>> showParam(HttpServletRequest request, UpdateData uc, String id) throws SQLException {
        request.setAttribute("action","Refactor");
            String sql = "select id,name,program_id from transfer where id ='" + id + "'";
            ArrayList<ArrayList<String>> list = uc.select(sql);
            sql = "select time, day from program where id ='" + list.get(0).get(2) + "'";
            ArrayList<String> temp = uc.select(sql).get(0);
            list.get(0).remove(2);
            list.get(0).addAll(temp);
            return list;
    }

   private boolean chekExistTransfer(HttpServletRequest request, UpdateData uc, String id) throws SQLException {
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

    private ArrayList<ArrayList<String>> makeList(UpdateData uc) throws SQLException {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ArrayList<ArrayList<String>> transfers = uc.select("select id, chanel_id, name, program_id from transfer");
        for(int row=0;row<transfers.size();row++) {
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < transfers.get(row).size(); i++) {
                if (i==3) {
                    String pid = transfers.get(row).get(i);
                    String sql = "select time, day from program where id ='" + pid + "'";
                    temp.addAll(uc.select(sql).get(0));
                } else
                if (i==0 | i==2) {
                    temp.add(transfers.get(row).get(i));
                } else {
                    String cid = transfers.get(row).get(i);
                    String sql = "select name from chanels where id ='" + cid + "'";
                    temp.add(uc.select(sql).get(0).get(0));
                }

            }
            list.add(temp);
        }
        return list;
    }
    }
