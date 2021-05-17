package AdminManipulation;

import Additional.LoginAdmin;
import Database.UpdateData;
import Exceptions.NoChannel;
import Exceptions.NoNameTransfer;
import Exceptions.TheSameName;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "TransferController", value = "/TransferController")
public class TransferController extends LoginAdmin {
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
        String action =request.getParameter("action");
        String id = request.getParameter("id");
        ArrayList<String> Dbparams = getparam();
        try (UpdateData uc = new UpdateData(Dbparams.get(0), Dbparams.get(1), Dbparams.get(2), Dbparams.get(3)))  {
            if(action!=null) {
                switch (action) {
                    case ("delete"):
                        LOCK.writeLock().lock();
                        try {
                            uc.delete("delete from transfer where id ='" + id + "'");
                        }finally {
                            LOCK.writeLock().unlock();
                        }
                        break;
                    case ("Add"):
                        LOCK.readLock().lock();
                        try {
                            ArrayList<String> data = uc.getMeta("transfer");
                            request.setAttribute("selected", data);
                            request.setAttribute("meta", data);
                        }finally {
                            LOCK.readLock().unlock();
                        }
                        request.setAttribute("action","doAdd");
                        break;
                    case ("refactor"):
                        LOCK.readLock().lock();
                        try {
                            ArrayList<String> dataRef = uc.select("select * from transfer where id ='" + id + "'").get(0);
                            String name = uc.select("select name from chanels where id ='" + dataRef.get(1) + "'").get(0).get(0);
                            dataRef.set(1, name);
                            request.setAttribute("selected", dataRef);
                            request.setAttribute("id", id);
                            request.setAttribute("meta",uc.getMeta("transfer"));
                            request.setAttribute("action","doRefactor");
                        }finally {
                            LOCK.readLock().unlock();
                        }
                        break;
                    case("doAdd"):
                        LOCK.writeLock().lock();
                        try {
                            doAdd(request, uc);
                        }finally {
                            LOCK.writeLock().unlock();
                        }
                        break;
                    case("doRefactor"):
                        LOCK.writeLock().lock();
                        try {
                        doRefactor(request,uc,id);
                        }finally {
                            LOCK.writeLock().unlock();
                        }
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            response.sendError(500);
            return;
        } catch (NoChannel e){
            request.setAttribute("exception","noChannel");
        } catch (NoNameTransfer e) {
            request.setAttribute("exception","noNameTransfer");
        } catch (TheSameName e){
            request.setAttribute("exception","sameTransfer");
        }
        try (UpdateData uc = new UpdateData(Dbparams.get(0), Dbparams.get(1), Dbparams.get(2), Dbparams.get(3))){
            ArrayList<ArrayList<String>> list = uc.select("select id, name from transfer");
            request.setAttribute("trlist", list);
        } catch (SQLException | ClassNotFoundException throwables) {
            response.sendError(500);
            return;
        }
        request.setAttribute("controller", "transfers");
        RequestDispatcher rd = request.getRequestDispatcher("adminpage.jsp");
        rd.forward(request,response);
    }


    public void doAdd(HttpServletRequest request, UpdateData uc) throws SQLException, NoChannel, NoNameTransfer, TheSameName {
        if(request.getParameter("name").equals("")){
            throw new NoNameTransfer("NoNameTransfer");
        }
        if(!checkChannel(uc, request.getParameter("chanel_id"))){
            throw new NoChannel("NoChannel");
        }
        String name = request.getParameter("name");
        String channel = request.getParameter("chanel_id");
        if(checkTransfer(uc,name,channel)){
            throw new TheSameName("sameTransfer");
        }
            ArrayList<String> params = new ArrayList<>();
            ArrayList<String> meta = uc.getMeta("transfer");
            StringBuffer sb = new StringBuffer();
            for (int i = 1; i < meta.size() - 1; i++) {
                sb.append("`" + meta.get(i) + "`");
                sb.append(",");
                String param = request.getParameter(meta.get(i));
                if (meta.get(i).equals("chanel_id")) {
                    String chanel_id = uc.select("select id from chanels where name = '" + param + "'").get(0).get(0);
                    params.add(chanel_id);
                } else {
                    params.add(param);
                }
            }
            sb.delete(sb.length() - 1, sb.length());
            String columns = sb.toString();
            sb = new StringBuffer();
            for (String param : params) {
                sb.append("'");
                sb.append(param);
                sb.append("'");
                sb.append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
            String values = sb.toString();
            uc.add("insert into transfer (" + columns + ") values (" + values + ")");
        }

    private boolean checkTransfer(UpdateData uc, String name, String channel) throws SQLException {
            String id = uc.select("select id from chanels where name ='" + channel + "'").get(0).get(0);
            ArrayList<ArrayList<String>> transfers = uc.select("select id from transfer " +
                    "where name ='" + name + "' and chanel_id ='" + id + "'");
            if (transfers.size() != 0) {
                return true;
            }
            return false;
    }
    private boolean checkChannel(UpdateData uc, String name) throws SQLException {
            ArrayList<ArrayList<String>> chanels = uc.select("select id from chanels where name ='" + name + "'");
            if (chanels.size() != 0) {
                return true;
            }
            return false;
    }


    public void doRefactor(HttpServletRequest request, UpdateData uc, String id) throws SQLException, NoNameTransfer, NoChannel, TheSameName {
        if(request.getParameter("name").equals("")){
            throw new NoNameTransfer("NoNameTransfer");
        }
        if(!checkChannel(uc, request.getParameter("chanel_id"))){
            throw new NoChannel("NoChannel");
        }
        String name = request.getParameter("name");
        String channel = request.getParameter("chanel_id");
        if(checkTransfer(uc,name,channel)){
            throw new TheSameName("sameTransfer");
        }
            ArrayList<String> metaRef = uc.getMeta("transfer");
            ArrayList<String> curr = uc.select("select * from transfer where id ='" + id + "'").get(0);
            for (int i = 1; i < metaRef.size() - 1; i++) {
                String param = request.getParameter(metaRef.get(i));
                if (metaRef.get(i).equals("chanel_id")) {
                    String chanel_id = uc.select("select id from chanels where name ='" + param + "'").get(0).get(0);
                    if (!curr.get(i).equals(chanel_id)) {
                        uc.add("update transfer set chanel_id ='" + chanel_id + "'" + "where id ='" + id + "'");
                    }
                } else {
                    if (!curr.get(i).equals(param)) {
                        uc.add("update transfer set " + metaRef.get(i) + " = '" + param + "' where id ='" + id + "'");
                    }
                }

            }
        }
    }
